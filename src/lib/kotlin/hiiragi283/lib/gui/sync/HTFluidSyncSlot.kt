package hiiragi283.lib.gui.sync

import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.util.HTDelegates
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableFluidStack](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/sync/SyncableFluidStack.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTFluidSyncSlot(property: ReadWriteProperty<Any?, FluidStack>) : HTIntSyncSlot {
    constructor(getter: Supplier<FluidStack>, setter: Consumer<FluidStack>) : this(HTDelegates.LazyDelegate(getter, setter))

    constructor(property: KMutableProperty0<FluidStack>) : this(HTDelegates.LazyDelegate(property::get, property::set))

    constructor(tank: HTBasicFluidTank) : this(tank::getStackCopy, tank::setStack)

    private var lastStack: FluidStack = FluidStack.EMPTY

    var asFluidStack: FluidStack by property

    override var amountAsInt: Int
        get() = asFluidStack.amount
        set(value) {
            asFluidStack = asFluidStack.copyWithAmount(value)
        }

    override fun getChange(): HTChangeType? {
        val current: FluidStack = this.asFluidStack
        if (current.isEmpty && lastStack.isEmpty) {
            return null
        }
        val sameFluid: Boolean = FluidStack.isSameFluidSameComponents(current, lastStack)
        if (!sameFluid || this.amountAsInt != this.lastStack.amount) {
            this.lastStack = current.copy()
            return when {
                sameFluid -> HTChangeType.PARTIAL
                else -> HTChangeType.FULL
            }
        }
        return null
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload = when (changeType) {
        HTChangeType.PARTIAL -> HTIntSyncPayload(this.amountAsInt)
        HTChangeType.FULL -> HTFluidSyncPayload(this.asFluidStack.copy())
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): FluidStack = asFluidStack

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: FluidStack) {
        asFluidStack = value
    }

    override fun toString(): String = "HTFluidSyncSlot(stack=$asFluidStack)"
}
