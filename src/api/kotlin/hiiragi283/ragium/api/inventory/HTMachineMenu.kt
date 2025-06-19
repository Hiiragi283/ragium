package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory

abstract class HTMachineMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    val definition: HTMenuDefinition,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
        pos,
    ) {
    val progress: Float
        get() {
            val totalTick: Int = definition.getData(0)
            val maxTicks: Int = definition.getData(1)
            if (maxTicks == 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    fun addUpgradeSlots() {
        val upgrades = definition.upgrades
        addSlot(upgrades, 0, 8.0, -0.5)
        addSlot(upgrades, 1, 8.0, 0.5)
        addSlot(upgrades, 2, 8.0, 1.5)
        addSlot(upgrades, 3, 8.0, 2.5)
    }
}
