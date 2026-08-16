package hiiragi283.lib.transfer

data object HTStorageValidators {
    @JvmStatic
    fun validateCapacity(capacity: Int): Int {
        check(capacity >= 0) { "Capacity must be non negative" }
        return capacity
    }

    @JvmStatic
    fun validateLimit(limit: Int): Int {
        check(limit >= 0) { "Limit must be non negative" }
        return limit
    }
}
