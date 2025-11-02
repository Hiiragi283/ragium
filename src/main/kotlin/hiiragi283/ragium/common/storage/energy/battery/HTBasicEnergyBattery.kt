package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.Predicate
import kotlin.math.min

/**
 * @see mekanism.common.capabilities.energy.BasicEnergyContainer
 */
open class HTBasicEnergyBattery(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTEnergyBattery.Basic() {
    companion object {
        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Int): HTBasicEnergyBattery =
            create(listener, capacity, HTStorageAccess.NOT_EXTERNAL, HTPredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicEnergyBattery =
            create(listener, capacity, HTPredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
        ): HTBasicEnergyBattery = HTBasicEnergyBattery(capacity, canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Int = 0

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (this.amount == 0) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = min(amount, getCapacity())
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Int = amount

    override fun getCapacity(): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.AMOUNT, getAmount())
    }

    override fun deserialize(input: HTValueInput) {
        input.getInt(RagiumConst.AMOUNT, 0).let(::setAmountUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
