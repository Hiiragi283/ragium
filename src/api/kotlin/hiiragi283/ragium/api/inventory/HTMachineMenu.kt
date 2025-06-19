package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.neoforged.neoforge.items.IItemHandler

abstract class HTMachineMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    upgrades: IItemHandler,
    protected val containerData: ContainerData,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
        pos,
    ) {
    val machine: HTMachineBlockEntity? = level.getBlockEntity(pos) as? HTMachineBlockEntity

    val progress: Float get() {
        val totalTick: Int = containerData.get(0)
        val maxTicks: Int = containerData.get(1)
        if (maxTicks == 0) return 0f
        val fixedTotalTicks: Int = totalTick % maxTicks
        return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
    }

    fun addUpgradeSlots(upgrades: IItemHandler) {
        addSlot(upgrades, 0, 8.0, -0.5)
        addSlot(upgrades, 1, 8.0, 0.5)
        addSlot(upgrades, 2, 8.0, 1.5)
        addSlot(upgrades, 3, 8.0, 2.5)
    }
}
