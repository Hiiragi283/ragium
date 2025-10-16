package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see [mekanism.common.inventory.slot.IFluidHandlerSlot]
 */
interface HTFluidItemSlot : HTItemSlot {
    fun getFluidTank(): HTFluidTank

    var isDraining: Boolean
    var isFilling: Boolean

    fun fillTank(): Boolean {
        if (this.getAmountAsInt() != 1) return false
        val handlerItem: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(getStack()) ?: return false
        val handler: HTFluidHandler = RagiumCapabilities.FLUID.getSlottedCapability(getStack()) ?: return false
        val tanks: List<HTFluidTank> = handler.getFluidTanks(handler.getFluidSideFor())
        if (tanks.size != 1) return false
        val tank: HTFluidTank = tanks[0]
        val stackIn: ImmutableFluidStack = tank.getStack()
        if (!stackIn.isEmpty() && getFluidTank().isValid(stackIn)) {
            if (fillHandlerFromOther(getFluidTank(), tank, stackIn)) {
                replaceContainer(handlerItem.container)
                return true
            }
        }
        return false
    }

    fun replaceContainer(container: ItemStack)

    private fun fillHandlerFromOther(toFill: HTFluidTank, toDrain: HTFluidTank, stack: ImmutableFluidStack): Boolean {
        val simulatedDrain: ImmutableFluidStack = toDrain.extract(stack.amountAsInt(), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (simulatedDrain.isEmpty()) return false
        val remainder: ImmutableFluidStack = getFluidTank().insert(simulatedDrain, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        val remainderAmount: Int = remainder.amountAsInt()
        val drained: Int = simulatedDrain.amountAsInt()
        if (remainderAmount < drained) {
            toFill.insert(
                toDrain.extract(
                    drained - remainderAmount,
                    HTStorageAction.EXECUTE,
                    HTStorageAccess.INTERNAL,
                ),
                HTStorageAction.EXECUTE,
                HTStorageAccess.INTERNAL,
            )
            return true
        }
        return false
    }
}
