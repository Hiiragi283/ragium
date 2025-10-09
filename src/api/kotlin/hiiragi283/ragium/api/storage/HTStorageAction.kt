package hiiragi283.ragium.api.storage

@JvmInline
value class HTStorageAction private constructor(val execute: Boolean) {
    companion object {
        val SIMULATE = HTStorageAction(false)

        val EXECUTE = HTStorageAction(true)

        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }
    }
}
