package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.createSpawnerStack
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getHTBlockEntity
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.*
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.item.component.HTSpawnerContent
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object RagiumGameEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    fun onRegisterCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registered Commands!")
    }

    @SubscribeEvent
    fun useItemOnBlock(event: UseItemOnBlockEvent) {
        if (event.isCanceled) return
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        if (stack.`is`(Items.BOOK)) {
            togglePreview(event)
        } else if (stack.`is`(Tags.Items.NETHER_STARS)) {
            captureSpawner(event)
        }
    }

    @JvmStatic
    private fun togglePreview(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        (level.getHTBlockEntity(pos) as? HTMultiblockController)?.let { controller: HTMultiblockController ->
            controller.showPreview = !controller.showPreview
            event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
        }
    }

    @JvmStatic
    private fun captureSpawner(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        val player: Player? = event.player
        val definition = HTControllerDefinition(level, pos, Direction.NORTH)
        val controller: HTMultiblockController = object : HTMultiblockController {
            override var showPreview: Boolean = false

            override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.SPAWNER

            override fun getDefinition(): HTControllerDefinition = definition

            override fun processData(definition: HTControllerDefinition, data: HTMultiblockData) {
                // Get spawner data
                val content: HTSpawnerContent =
                    data.components.get(RagiumComponentTypes.SPAWNER_CONTENT.get()) ?: return
                // Break structure
                getMultiblockMap().convertAbsolute(definition).forEach { posIn: BlockPos, _: HTMultiblockComponent ->
                    level.destroyBlock(posIn, false)
                }
                // Drop Spawner
                dropStackAt(level, pos.above(), createSpawnerStack(content.entityType))
                event.itemStack.consume(1, player)
            }
        }
        val data: HTMultiblockData = controller.collectData { player?.displayClientMessage(it, true) }
        controller.processData(definition, data)
        event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
    }

    @SubscribeEvent
    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickEmpty) {
        val stack: ItemStack = event.itemStack
        if (!stack.isEmpty && stack.`is`(RagiumItems.POTION_BUNDLE)) {
            PacketDistributor.sendToServer(HTPotionBundlePacket)
        }
    }

    @SubscribeEvent
    fun onBlastFurnaceSucceeded(event: HTMachineProcessEvent.Success) {
        val machine: HTMachineAccess = event.machine
        val level: Level = machine.levelAccess ?: return
        val pos: BlockPos = machine.pos
        var addon: HTSlagCollectorBlockEntity? = null
        if (machine.machineType == HTMachineType.BLAST_FURNACE) {
            for (direction: Direction in Direction.entries) {
                addon = (level.getBlockEntity(pos.relative(direction)) as? HTSlagCollectorBlockEntity)
                if (addon != null) break
            }
        }
        if (addon == null) return
        addon.onReceiveEvent(event)
    }
}
