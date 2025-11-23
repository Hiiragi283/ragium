package hiiragi283.ragium.api.util

/**
 * 変化が起きた時に呼び出されるインターフェース
 * @see mekanism.api.IContentsListener
 */
fun interface HTContentListener : Runnable {
    fun onContentsChanged()

    fun compose(other: Runnable): HTContentListener = HTContentListener {
        other.run()
        this.onContentsChanged()
    }

    fun andThen(other: Runnable): HTContentListener = HTContentListener {
        this.onContentsChanged()
        other.run()
    }

    override fun run() {
        onContentsChanged()
    }

    interface Empty : HTContentListener {
        override fun onContentsChanged() {}
    }
}
