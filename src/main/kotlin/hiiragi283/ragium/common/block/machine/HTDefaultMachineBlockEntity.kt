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

class HTDefaultMachineBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntity(RagiumBlockEntityTypes.DEFAULT_MACHINE, pos, state) {
    override val itemHandler: ItemStackHandler = ItemStackHandler(5)
    override val tanks: Array<out HTTieredFluidTank> = Array(2) { HTTieredFluidTank(machineTier) }
    override val processor: HTRecipeProcessor = HTMachineRecipeProcessor.fromMachine(
        this,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        intArrayOf(),
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler.basic(itemHandler)

    override fun getFluidHandler(direction: Direction?): LimitedFluidHandler = LimitedFluidHandler.small(tanks)
}
