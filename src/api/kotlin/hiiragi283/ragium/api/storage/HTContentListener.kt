package hiiragi283.ragium.api.storage

/**
 * @see [mekanism.api.IContentsListener]
 */
fun interface HTContentListener {
    companion object {
        @JvmField
        val NONE = HTContentListener {}
    }

    fun onContentsChanged()
}
