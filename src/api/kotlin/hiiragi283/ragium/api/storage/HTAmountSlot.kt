package hiiragi283.ragium.api.storage

sealed interface HTAmountSlot<N> where N : Number, N : Comparable<N> {
    fun isEmpty(): Boolean

    fun insert(amount: N, action: HTStorageAction, access: HTStorageAccess): N

    fun extract(amount: N, action: HTStorageAction, access: HTStorageAccess): N

    interface IntSized :
        HTAmountSlot<Int>,
        HTAmountView.IntSized {
        override fun isEmpty(): Boolean = getAmount() <= 0
    }

    interface LongSized :
        HTAmountSlot<Long>,
        HTAmountView.LongSized {
        override fun isEmpty(): Boolean = getAmount() <= 0
    }
}
