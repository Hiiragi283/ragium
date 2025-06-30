package hiiragi283.ragium.api.storage.fluid

import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class HTFilteredFluidHandler(private val delegate: List<FluidTank>, private val filter: HTFluidFilter) : HTFluidHandler {
    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult {
        for (index: Int in tankRange.toList().reversed()) {
            val tank: FluidTank = delegate[index]
            if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun getTanks(): Int = delegate.size

    override fun getFluidInTank(tank: Int): FluidStack = delegate[tank].fluid

    override fun getTankCapacity(tank: Int): Int = delegate[tank].capacity

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegate[tank].isFluidValid(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (!filter.canFill(delegate, resource)) return 0
        for (tank: FluidTank in delegate) {
            val filled: Int = tank.fill(resource, action)
            if (filled > 0) return filled
        }
        return 0
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!filter.canDrain(delegate, resource)) return FluidStack.EMPTY
        for (tank: FluidTank in delegate) {
            val drained: FluidStack = tank.drain(resource, action)
            if (!drained.isEmpty) return drained
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!filter.canDrain(delegate, maxDrain)) return FluidStack.EMPTY
        for (tank: FluidTank in delegate) {
            val drained: FluidStack = tank.drain(maxDrain, action)
            if (!drained.isEmpty) return drained
        }
        return FluidStack.EMPTY
    }
}
