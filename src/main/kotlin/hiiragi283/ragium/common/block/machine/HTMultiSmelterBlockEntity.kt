package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntity
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.inventory.HTMultiSmelterContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntity(RagiumBlockEntityTypes.MULTI_SMELTER, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.MULTI_SMELTER

    override val itemHandler: ItemStackHandler = ItemStackHandler(2)
    override val tanks: Array<out HTMachineFluidTank> = arrayOf()

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMultiSmelterContainerMenu(containerId, playerInventory, blockPos, itemHandler)

    override fun getItemHandler(direction: Direction?): IItemHandler = LimitedItemHandler.Companion.small(itemHandler)
}
