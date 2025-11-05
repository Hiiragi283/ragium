package hiiragi283.ragium.common.storage.experience.tank

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.function.clamp
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.Predicate

open class HTBasicExperienceTank(
    private val capacity: Long,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTExperienceTank.Basic() {
    companion object {
        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Long): HTBasicExperienceTank =
            create(listener, capacity, HTStorageAccess.Companion.NOT_EXTERNAL, HTPredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Long): HTBasicExperienceTank =
            create(listener, capacity, HTPredicates.alwaysTrue(), HTStorageAccess.Companion.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Long,
            canExtract: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
        ): HTBasicExperienceTank = HTBasicExperienceTank(capacity, canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Long = 0

    override fun setAmount(amount: Long) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Long, validate: Boolean = false) {
        if (amount == 0L) {
            if (this.amount == 0L) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = amount.clamp(0..getCapacity())
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Long = amount

    override fun getCapacity(): Long = capacity

    override fun serialize(output: HTValueOutput) {
        output.putLong(RagiumConst.AMOUNT, getAmount())
    }

    override fun deserialize(input: HTValueInput) {
        input.getLong(RagiumConst.AMOUNT, 0).let(::setAmountUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
