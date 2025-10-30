package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.Predicate
import kotlin.math.min

open class HTBasicExperienceStorage(
    private val capacity: Long,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTExperienceStorage.Basic() {
    companion object {
        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Long): HTBasicExperienceStorage =
            create(listener, capacity, HTStorageAccess.NOT_EXTERNAL, HTPredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Long): HTBasicExperienceStorage =
            create(listener, capacity, HTPredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Long,
            canExtract: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
        ): HTBasicExperienceStorage = HTBasicExperienceStorage(capacity, canExtract, canInsert, listener)
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
        } else if (!validate) {
            this.amount = min(amount, getCapacity())
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
        input.getLong(RagiumConst.AMOUNT)?.let(::setAmountUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
