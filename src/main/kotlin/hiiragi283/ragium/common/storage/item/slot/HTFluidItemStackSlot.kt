package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.insertFluid
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.FluidInventorySlot]
 */
open class HTFluidItemStackSlot(
    protected val tank: HTFluidTank,
    canExtract: Predicate<HTItemStorageStack>,
    canInsert: Predicate<HTItemStorageStack>,
    filter: Predicate<HTItemStorageStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(HTItemSlot.ABSOLUTE_MAX_STACK_SIZE, canExtract, canInsert, filter, listener, x, y, slotType),
    HTFluidItemSlot {
    companion object {
        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<HTItemStorageStack> = Predicate { stack: HTItemStorageStack ->
            val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(stack) ?: return@Predicate false
            for (i: Int in handler.tankRange) {
                val stackIn: FluidStack = handler.getFluidInTank(i)
                if (!stack.isEmpty() &&
                    tank.insertFluid(stackIn, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL).amount < stackIn.amount
                ) {
                    return@Predicate true
                }
            }
            false
        }
    }

    final override fun getFluidTank(): HTFluidTank = tank

    override var isDraining: Boolean = false
    override var isFilling: Boolean = false

    override fun setStack(stack: HTItemStorageStack) {
        super.setStack(stack)
        isDraining = false
        isFilling = false
    }

    override fun serialize(output: HTValueOutput) {
        super<HTFluidItemSlot>.serialize(output)
        output.putBoolean("draining", isDraining)
        output.putBoolean("filling", isFilling)
    }

    override fun deserialize(input: HTValueInput) {
        this.isDraining = input.getBoolean("draining", false)
        this.isFilling = input.getBoolean("filling", false)
        super.deserialize(input)
    }
}
