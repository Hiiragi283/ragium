package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.FluidInventorySlot]
 */
open class HTFluidItemStackSlot protected constructor(
    protected val tank: HTFluidTank,
    canExtract: Predicate<ImmutableItemStack>,
    canInsert: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTItemStackSlot(
        RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
        canExtract,
        canInsert,
        ImmutableStack.alwaysTrue(),
        listener,
        x,
        y,
        HTContainerItemSlot.Type.BOTH,
    ),
    HTFluidItemSlot {
    companion object {
        @JvmStatic
        fun inputPredicate(tank: HTFluidTank): Predicate<ImmutableItemStack> = Predicate { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(stack) ?: return@Predicate false
            val views: List<HTStackView<ImmutableFluidStack>> = RagiumCapabilities.FLUID.getCapabilityViews(stack)
            var hasEmpty = false
            for (view: HTStackView<ImmutableFluidStack> in views) {
                val stackIn: ImmutableFluidStack = view.getStack()
                if (view.isEmpty()) {
                    hasEmpty = true
                } else if (tank.insert(stackIn, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).amountAsInt() <
                    view.getAmountAsInt()
                ) {
                    return@Predicate true
                }
            }

            if (tank.isEmpty()) {
                return@Predicate hasEmpty
            }

            var stack: ImmutableFluidStack = tank.getStack()
            if (stack.amountAsInt() < FluidType.BUCKET_VOLUME) {
                stack = stack.copyWithAmount(FluidType.BUCKET_VOLUME)
            } else {
                stack = stack.copy()
            }
            return@Predicate handler.fill(stack.stack, HTStorageAction.SIMULATE.toFluid()) > 0
        }

        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<ImmutableItemStack> = Predicate { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(stack) ?: return@Predicate false
            for (i: Int in (0..<handler.tanks)) {
                val stackIn: FluidStack = handler.getFluidInTank(i)
                if (stackIn.isEmpty) continue
                if (tank.insert(stackIn.toImmutable(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL).amountAsInt() >=
                    stack.amountAsInt()
                ) {
                    continue
                }
                return@Predicate true
            }
            false
        }

        @JvmStatic
        fun create(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemStackSlot = create(tank, listener, x, y, ImmutableStack.alwaysTrue(), inputPredicate(tank))

        @JvmStatic
        fun fill(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemStackSlot = create(tank, listener, x, y, ImmutableStack.alwaysFalse(), fillPredicate(tank))

        @JvmStatic
        fun drain(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidItemStackSlot = create(tank, listener, x, y, ImmutableStack.alwaysFalse()) { stack: ImmutableItemStack ->
            val handler: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(stack) ?: return@create false
            if (tank.isEmpty()) {
                for (i: Int in (0..<handler.tanks)) {
                    if (handler.getFluidInTank(i).amount < handler.getTankCapacity(i)) {
                        return@create true
                    }
                }
            }
            handler.fill(tank.getFluidStack(), HTStorageAction.SIMULATE.toFluid()) > 0
        }

        @JvmStatic
        private fun create(
            tank: HTFluidTank,
            listener: HTContentListener?,
            x: Int,
            y: Int,
            canExtract: Predicate<ImmutableItemStack> = ImmutableStack.alwaysTrue(),
            canInsert: Predicate<ImmutableItemStack> = inputPredicate(tank),
        ): HTFluidItemStackSlot = HTFluidItemStackSlot(tank, canExtract, canInsert, listener, x, y)
    }

    final override fun getFluidTank(): HTFluidTank = tank

    override var isDraining: Boolean = false
    override var isFilling: Boolean = false

    override fun setStack(stack: ImmutableItemStack) {
        super.setStack(stack)
        isDraining = false
        isFilling = false
    }

    override fun serialize(output: HTValueOutput) {
        super.serialize(output)
        output.putBoolean("draining", isDraining)
        output.putBoolean("filling", isFilling)
    }

    override fun deserialize(input: HTValueInput) {
        this.isDraining = input.getBoolean("draining", false)
        this.isFilling = input.getBoolean("filling", false)
        super.deserialize(input)
    }
}
