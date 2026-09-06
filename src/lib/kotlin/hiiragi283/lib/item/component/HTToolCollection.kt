package hiiragi283.lib.item.component

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
data class HTToolCollection<out T>(val sword: T, val shovel: T, val pickaxe: T, val axe: T, val hoe: T) :
    AbstractCollection<T>() {
    companion object {
        @JvmField
        val VALUES: HTToolCollection<HTToolType> =
            HTToolCollection(HTToolType.SWORD, HTToolType.SHOVEL, HTToolType.PICKAXE, HTToolType.AXE, HTToolType.HOE)
    }

    operator fun get(toolType: HTToolType): T = when (toolType) {
        HTToolType.SWORD -> sword
        HTToolType.SHOVEL -> shovel
        HTToolType.PICKAXE -> pickaxe
        HTToolType.AXE -> axe
        HTToolType.HOE -> hoe
    }

    fun asSequence(): Sequence<T> = HTToolType.entries.asSequence().map(::get)

    override val size: Int = 5

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = asSequence().iterator()

    inline fun <R> map(transform: (T) -> R): HTToolCollection<R> = HTToolCollection(
        transform(sword),
        transform(shovel),
        transform(pickaxe),
        transform(axe),
        transform(hoe)
    )

    inline fun <U, R> zip(other: HTToolCollection<U>, transform: (T, U) -> R): HTToolCollection<R> = HTToolCollection(
        transform(sword, other.sword),
        transform(shovel, other.shovel),
        transform(pickaxe, other.pickaxe),
        transform(axe, other.axe),
        transform(hoe, other.hoe)
    )
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
inline fun <T> HTToolCollection(init: (toolType: HTToolType) -> T): HTToolCollection<T> =
    HTToolCollection.VALUES.map(init)
