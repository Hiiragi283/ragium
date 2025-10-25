package hiiragi283.ragium.common.storage.energy

import com.google.common.base.Predicates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.Predicate
import kotlin.math.min

/**
 * @see mekanism.common.capabilities.energy.BasicEnergyContainer
 */
open class HTBasicEnergyStorage(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTEnergyStorage.Mutable() {
    companion object {
        @JvmField
        val INTERNAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == HTStorageAccess.INTERNAL }

        @JvmField
        val MANUAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == HTStorageAccess.MANUAL }

        @JvmField
        val NOT_EXTERNAL: Predicate<HTStorageAccess> = Predicate { it != HTStorageAccess.EXTERNAL }

        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Int): HTBasicEnergyStorage =
            create(listener, capacity, NOT_EXTERNAL, Predicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicEnergyStorage =
            create(listener, capacity, Predicates.alwaysTrue(), INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = Predicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = Predicates.alwaysTrue(),
        ): HTBasicEnergyStorage = HTBasicEnergyStorage(capacity, canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Int = 0

    override fun setAmountAsInt(amount: Int) {
        check(amount >= 0) { "Energy cannot be negative" }
        val fixedAmount: Int = min(amount, getCapacityAsInt())
        if (this.amount != fixedAmount) {
            this.amount = fixedAmount
            onContentsChanged()
        }
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmountAsInt(): Int = amount

    override fun getCapacityAsInt(): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.AMOUNT, getAmountAsInt())
    }

    override fun deserialize(input: HTValueInput) {
        input.getInt(RagiumConst.AMOUNT)?.let(::setAmountAsInt)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
