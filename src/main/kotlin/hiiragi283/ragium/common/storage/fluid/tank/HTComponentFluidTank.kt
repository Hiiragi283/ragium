package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.fluid.ComponentBackedFluidTank
 */
open class HTComponentFluidTank(
    private val parent: ItemStack,
    private val capacity: Int,
    private val canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableFluidStack>,
) : HTFluidTank.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun create(
            parent: ItemStack,
            capacity: Int,
            canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableFluidStack> = HTPredicates.alwaysTrue(),
        ): HTComponentFluidTank = HTComponentFluidTank(parent, capacity, canExtract, canInsert, filter)
    }

    protected val component: DataComponentType<ImmutableFluidStack> get() = RagiumDataComponents.FLUID_CONTENT

    final override fun isValid(stack: ImmutableFluidStack): Boolean = this.filter.test(stack)

    final override fun isStackValidForInsert(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && this.canInsert.test(stack, access)

    final override fun canStackExtract(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && this.canExtract.test(stack, access)

    final override fun getStack(): ImmutableFluidStack? = parent.get(component)

    override fun getCapacity(stack: ImmutableFluidStack?): Int = capacity

    final override fun setStack(stack: ImmutableFluidStack?) {
        parent.set(component, stack)
    }

    override fun updateCount(stack: ImmutableFluidStack, amount: Int) {
        setStack(stack.copyWithAmount(amount))
    }
}
