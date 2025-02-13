package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent
import net.neoforged.neoforge.items.IItemHandlerModifiable
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object RagiumGameEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registered Commands!")
    }

    @JvmStatic
    var currentServer: MinecraftServer? = null
        private set

    @SubscribeEvent
    fun onServerStart(event: ServerAboutToStartEvent) {
        currentServer = event.server
        LOGGER.info("Current server updated!")
    }

    @SubscribeEvent
    fun onStoppedStart(event: ServerStoppedEvent) {
        currentServer = null
        LOGGER.info("Current server removed...")
    }

    @SubscribeEvent
    fun onEntityDrops(event: LivingDropsEvent) {
        if (event.isCanceled) return
        val source: DamageSource = event.source
        val attacker: Player = source.entity as? Player ?: return
        val level: Level = attacker.level()
        if (!level.isClientSide) {
            val stackInHands: Iterable<ItemStack> = attacker.handSlots
            for (stack: ItemStack in stackInHands) {
                if (stack.isEmpty) continue
                if (stack.getLevel(level.registryAccess(), Enchantments.SILK_TOUCH) > 0) {
                    val attacked: LivingEntity = event.entity
                    attacked.type
                        .builtInRegistryHolder()
                        .getData(RagiumDataMaps.EXECUTIONER_DROPS)
                        ?.let { ItemEntity(level, attacked.x, attacked.y, attacked.z, it.copy()) }
                        ?.let(event.drops::add)
                }
            }
        }
    }

    @SubscribeEvent
    fun onBlastFurnaceSucceeded(event: HTMachineProcessEvent.Success) {
        val machine: HTMachineAccess = event.machine
        val level: Level = machine.levelAccess ?: return
        val pos: BlockPos = machine.pos
        var foundAddon: HTSlagCollectorBlockEntity? = null
        if (machine.machineType == HTMachineType.BLAST_FURNACE) {
            for (direction: Direction in Direction.entries) {
                foundAddon = (level.getBlockEntity(pos.relative(direction)) as? HTSlagCollectorBlockEntity)
                if (foundAddon != null) break
            }
        }
        if (foundAddon == null) return
        val itemHandler: IItemHandlerModifiable = foundAddon.getItemHandler(null)
        itemHandler.insertItem(0, RagiumItems.SLAG.toStack(), false)
    }
}
