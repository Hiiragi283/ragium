package hiiragi283.ragium.api.storage

import kotlin.math.min

abstract class HTSingleVariantStorage<T : HTVariant<*>> :
    HTSingleStorage<T>,
    HTStorageListener {
    override var resource: T = getEmptyVariant()
        protected set
    override var amount: Int = 0
        protected set

    protected abstract fun getEmptyVariant(): T

    abstract fun isValid(variant: T): Boolean

    override fun insert(resource: T, maxAmount: Int, simulate: Boolean): Int {
        if (resource.isEmpty) return 0
        if (maxAmount <= 0) return 0

        if (resource == this.resource || this.resource.isEmpty) {
            if (isValid(resource)) {
                val inserted: Int = min(maxAmount, capacity - amount)
                if (inserted > 0) {
                    if (!simulate) {
                        if (this.resource.isEmpty) {
                            this.resource = resource
                            this.amount = inserted
                        } else {
                            this.amount += inserted
                        }
                        onContentsChanged()
                    }
                    return inserted
                }
            }
        }
        return 0
    }

    override fun extract(resource: T, maxAmount: Int, simulate: Boolean): Int {
        if (resource.isEmpty) return 0
        if (maxAmount <= 0) return 0

        if (resource == this.resource && isValid(resource)) {
            val extracted: Int = min(maxAmount, amount)
            if (extracted > 0) {
                if (!simulate) {
                    amount -= extracted
                    if (amount <= 0) {
                        this.resource = getEmptyVariant()
                        this.amount = 0
                    }
                    onContentsChanged()
                }
                return extracted
            }
        }
        return 0
    }

    override val isEmpty: Boolean
        get() = resource.isEmpty

    fun clear() {
        resource = getEmptyVariant()
        amount = 0
    }
}
