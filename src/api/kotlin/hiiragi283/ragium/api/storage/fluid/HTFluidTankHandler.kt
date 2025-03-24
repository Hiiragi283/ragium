package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler

interface HTFluidTankHandler : IFluidHandler {
    fun getFluidIoFromSlot(tank: Int): HTStorageIO

    fun getFluidTank(tank: Int): HTFluidTank?

    fun getTankRange(): IntRange = (0 until tanks)

    fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult {
        for (index: Int in getTankRange().toList().reversed()) {
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            val storageIO: HTStorageIO = when (getFluidIoFromSlot(index).canInsert) {
                true -> HTStorageIO.GENERIC
                false -> HTStorageIO.OUTPUT
            }
            if (FluidUtil.interactWithFluidHandler(player, hand, storageIO.wrapFluidTank(tank))) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    //    IFluidHandler    //

    override fun getFluidInTank(tank: Int): FluidStack = getFluidTank(tank)?.stack ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int = getFluidTank(tank)?.capacity ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = getFluidTank(tank)?.isValid(HTFluidVariant.of(stack)) == true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        var filled = 0
        for (index: Int in getTankRange()) {
            if (!getFluidIoFromSlot(index).canInsert) continue
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            filled += tank.insert(resource, action.simulate())
            if (resource.isEmpty) break
        }
        return filled
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        var resourceIn: HTFluidVariant = HTFluidVariant.EMPTY
        var extracted = 0
        for (index: Int in getTankRange()) {
            if (!getFluidIoFromSlot(index).canExtract) continue
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            if (tank.amount <= maxDrain) {
                resourceIn = tank.resource
                extracted = tank.extract(maxDrain, action.simulate())
                if (extracted > 0) {
                    break
                } else {
                    resourceIn = HTFluidVariant.EMPTY
                }
            }
        }
        return when {
            extracted > 0 -> resourceIn.toStack(extracted)
            else -> FluidStack.EMPTY
        }
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        var extracted = 0
        for (index: Int in getTankRange()) {
            if (!getFluidIoFromSlot(index).canExtract) continue
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            if (tank.resource == HTFluidVariant.of(resource)) {
                extracted = tank.extract(resource.amount, action.simulate())
                if (extracted > 0) break
            }
        }
        return when {
            extracted > 0 -> resource.copyWithAmount(extracted)
            else -> FluidStack.EMPTY
        }
    }
}
