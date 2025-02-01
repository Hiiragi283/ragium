package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntity
import hiiragi283.ragium.api.capability.LimitedFluidHandler
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTDefaultMachineContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.items.ItemStackHandler

class HTDefaultProcessorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntity(RagiumBlockEntityTypes.DEFAULT_PROCESSOR, pos, state) {
    override val itemHandler: ItemStackHandler = ItemStackHandler(4)
    override val tanks: Array<out HTMachineFluidTank> =
        Array(2) { HTMachineFluidTank(FluidType.BUCKET_VOLUME * 8, this::setChanged) }
    override val processor: HTMachineRecipeProcessor = createMachineProcessor(
        intArrayOf(0, 1),
        intArrayOf(2, 3),
        intArrayOf(0),
        intArrayOf(1),
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTDefaultMachineContainerMenu(containerId, playerInventory, blockPos, itemHandler)

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler.basic(itemHandler)

    override fun getFluidHandler(direction: Direction?): LimitedFluidHandler = LimitedFluidHandler.small(tanks)
}
