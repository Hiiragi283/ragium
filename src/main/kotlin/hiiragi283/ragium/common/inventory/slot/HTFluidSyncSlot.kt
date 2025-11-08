package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.common.inventory.slot.payload.HTFluidSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import net.minecraft.core.RegistryAccess
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * @see mekanism.common.inventory.container.sync.SyncableFluidStack
 */
class HTFluidSyncSlot(private val getter: Supplier<ImmutableFluidStack?>, private val setter: Consumer<ImmutableFluidStack?>) :
    HTSyncableSlot {
    constructor(tank: HTFluidStackTank) : this(tank::getStack, tank::setStackUnchecked)

    private var lastStack: ImmutableFluidStack? = null

    fun getStack(): ImmutableFluidStack? = this.getter.get()

    fun setStack(stack: ImmutableFluidStack?) {
        this.setter.accept(stack)
    }

    private fun getAmount(): Int = getStack()?.amount() ?: 0

    fun setAmount(amount: Int) {
        setStack(getStack()?.copyWithAmount(amount))
    }

    override fun getChange(): HTChangeType {
        val current: ImmutableFluidStack? = this.getStack()
        val sameFluid: Boolean = current?.value() == this.lastStack?.value()
        if (!sameFluid || this.getAmount() != this.lastStack?.amount()) {
            this.lastStack = current
            return when {
                sameFluid -> HTChangeType.AMOUNT
                else -> HTChangeType.FULL
            }
        }
        return HTChangeType.EMPTY
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.EMPTY -> null
        HTChangeType.AMOUNT -> HTIntSyncPayload(this.getAmount())
        HTChangeType.FULL -> HTFluidSyncPayload(this.getStack())
    }
}
