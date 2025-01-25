package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Supplier

abstract class HTMachineContainerMenu(
    menuType: Supplier<out MenuType<*>>,
    containerId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    itemHandler: IItemHandler,
) : HTContainerMenu(
        menuType,
        containerId,
        playerInv,
        pos,
        itemHandler,
    ) {
    val machineEntity: HTMachineBlockEntity? = blockEntity as? HTMachineBlockEntity

    protected val containerData: ContainerData = machineEntity?.containerData ?: SimpleContainerData(2)

    fun getProgress(): Float = containerData.get(0).toFloat() / containerData.get(1).toFloat()
}
