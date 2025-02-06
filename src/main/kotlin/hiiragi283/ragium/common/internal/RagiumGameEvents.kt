package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.items.ItemStackHandler
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object RagiumGameEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registered Commands!")
    }

    @SubscribeEvent
    fun onBlastFurnaceSucceeded(event: HTMachineProcessEvent.Success) {
        val machine: HTMachineAccess = event.machine
        val level: Level = machine.levelAccess ?: return
        val pos: BlockPos = machine.pos
        var foundAddon: HTSlagCollectorBlockEntity? = null
        if (machine.machineKey == RagiumMachineKeys.BLAST_FURNACE) {
            for (direction: Direction in Direction.entries) {
                foundAddon = (level.getBlockEntity(pos.relative(direction)) as? HTSlagCollectorBlockEntity)
                if (foundAddon != null) break
            }
        }
        if (foundAddon == null) return
        val itemHandler: ItemStackHandler = foundAddon.getItemHandler(null)
        itemHandler.insertItem(0, RagiumItems.SLAG.toStack(), false)
    }
}
