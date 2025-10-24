package hiiragi283.ragium.common.storage.energy.battery

import com.google.common.base.Predicates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.LongSupplier
import java.util.function.Predicate
import kotlin.math.min

/**
 * @see mekanism.common.capabilities.energy.BasicEnergyContainer
 */
open class HTBasicEnergyBattery(
    private val capacity: LongSupplier,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTEnergyBattery.Mutable() {
    companion object {
        @JvmField
        val INTERNAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == HTStorageAccess.INTERNAL }

        @JvmField
        val MANUAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == HTStorageAccess.MANUAL }

        @JvmField
        val NOT_EXTERNAL: Predicate<HTStorageAccess> = Predicate { it != HTStorageAccess.EXTERNAL }

        @JvmStatic
        fun input(listener: HTContentListener?, capacity: LongSupplier): HTBasicEnergyBattery =
            create(listener, capacity, NOT_EXTERNAL, Predicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: LongSupplier): HTBasicEnergyBattery =
            create(listener, capacity, Predicates.alwaysTrue(), INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: LongSupplier,
            canExtract: Predicate<HTStorageAccess> = Predicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = Predicates.alwaysTrue(),
        ): HTBasicEnergyBattery = HTBasicEnergyBattery(capacity, canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Long = 0

    override fun setAmountAsLong(amount: Long) {
        check(amount >= 0) { "Energy cannot be negative" }
        val fixedAmount: Long = min(amount, getCapacityAsLong())
        if (this.amount != fixedAmount) {
            this.amount = fixedAmount
            onContentsChanged()
        }
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmountAsLong(): Long = amount

    override fun getCapacityAsLong(): Long = capacity.asLong

    override fun serialize(output: HTValueOutput) {
        output.putLong(RagiumConst.AMOUNT, getAmountAsLong())
    }

    override fun deserialize(input: HTValueInput) {
        input.getLong(RagiumConst.AMOUNT)?.let(::setAmountAsLong)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
