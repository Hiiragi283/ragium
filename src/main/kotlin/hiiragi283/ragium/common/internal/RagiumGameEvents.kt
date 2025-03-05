package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createSpawnerStack
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.multiblock.*
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.item.component.HTSpawnerContent
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.AnvilUpdateEvent
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
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
            // togglePreview(event)
        } else if (stack.`is`(Tags.Items.NETHER_STARS)) {
            captureSpawner(event)
        }
    }

    /*private fun togglePreview(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        (level.getHTBlockEntity(pos) as? HTMultiblockController)?.let { controller: HTMultiblockController ->
            controller.showPreview = !controller.showPreview
            event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
        }
    }*/

    @JvmStatic
    private fun captureSpawner(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        val player: Player? = event.player
        val definition = HTControllerDefinition(level, pos, Direction.NORTH)
        val controller: HTMultiblockController = object : HTMultiblockController {
            override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.SPAWNER

            override fun getDefinition(): HTControllerDefinition = definition

            override fun processData(definition: HTControllerDefinition, data: HTMultiblockData) {
                // Get spawner data
                val content: HTSpawnerContent = data.get(RagiumComponentTypes.SPAWNER_CONTENT.get()) ?: return
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
    
    fun onItemCrafted(event: PlayerEvent.ItemCraftedEvent) {
        val result: ItemStack = event.crafting
        if (result.isEmpty) return
        val access: RegistryAccess = event.entity.level().registryAccess()
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = access.enchLookup()
        val enchMap: Map<ResourceKey<Enchantment>, Int> = when {
            result.`is`(RagiumItems.EMBER_ALLOY_TOOLS.swordItem) -> mapOf(Enchantments.FIRE_ASPECT to 2)
            result.item in RagiumItems.EMBER_ALLOY_ARMORS -> mapOf(Enchantments.FIRE_PROTECTION to 2)
            else -> return
        }
        result.modifyEnchantment { mutable: ItemEnchantments.Mutable ->
            for ((key: ResourceKey<Enchantment>, value: Int) in enchMap) {
                enchLookup.get(key).ifPresent { mutable.set(it, value) }
            }
            mutable.toImmutable()
        }
    }

    @SubscribeEvent
    fun onAnvilUpdated(event: AnvilUpdateEvent) {
        if (event.isCanceled) return
    }
}
