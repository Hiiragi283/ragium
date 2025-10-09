package hiiragi283.ragium.api.storage

@JvmInline
value class HTStorageAction private constructor(val simulate: Boolean) {
    companion object {
        val SIMULATE = HTStorageAction(true)

        val EXECUTE = HTStorageAction(false)

        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }
    }

    val execute: Boolean get() = !simulate
}
