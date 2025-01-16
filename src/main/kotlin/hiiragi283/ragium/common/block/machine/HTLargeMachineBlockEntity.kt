package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntity
import hiiragi283.ragium.api.capability.LimitedFluidHandler
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.fluid.HTTieredFluidTank
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler

class HTLargeMachineBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntity(RagiumBlockEntityTypes.LARGE_MACHINE, pos, state) {
    override val itemHandler: ItemStackHandler = ItemStackHandler(7)
    override val tanks: Array<out HTTieredFluidTank> = Array(4) { HTTieredFluidTank(machineTier) }
    override val processor: HTRecipeProcessor = HTMachineRecipeProcessor.fromMachine(
        this,
        intArrayOf(0, 1, 2),
        intArrayOf(4, 5, 6),
        3,
        intArrayOf(),
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler.large(itemHandler)

    override fun getFluidHandler(direction: Direction?): LimitedFluidHandler = LimitedFluidHandler.basic(tanks)
}
