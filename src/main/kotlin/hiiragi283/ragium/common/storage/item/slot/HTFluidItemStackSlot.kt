package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
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
            val tanks: List<HTFluidTank> = RagiumCapabilities.FLUID.getCapabilitySlots(stack)
            if (tanks.isEmpty()) return@Predicate false
            var hasEmpty = false
            for (tankIn: HTFluidTank in tanks) {
                val stackIn: ImmutableFluidStack = tankIn.getStack()
                if (tankIn.isEmpty()) {
                    hasEmpty = true
                } else if (tank.insert(stackIn, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).amountAsInt() <
                    tankIn.getAmountAsInt()
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
            val handler: HTFluidHandler = RagiumCapabilities.FLUID.getSlottedCapability(stack) ?: return@Predicate false
            for (fluidTank: HTFluidTank in handler.getFluidTanks(handler.getFluidSideFor())) {
                val stack: ImmutableFluidStack = fluidTank.getStack()
                if (stack.isEmpty()) continue
                if (tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL).amountAsInt() >= stack.amountAsInt()) continue
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
