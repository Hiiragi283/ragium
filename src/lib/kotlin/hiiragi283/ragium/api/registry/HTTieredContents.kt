package hiiragi283.ragium.api.registry

import hiiragi283.lib.data.lang.HTLangPatternProvider

data class HTTieredContents<out T>(val basic: T, val advanced: T, val industrial: T) : AbstractCollection<T>() {
    companion object {
        @JvmField
        val NAMES: HTTieredContents<String> = HTTieredContents("basic", "advanced", "industrial")

        @JvmField
        val TRANSLATE_PATTERNS: HTTieredContents<HTLangPatternProvider> = HTTieredContents(
            HTLangPatternProvider("Basic %s", "基本%s"),
            HTLangPatternProvider("Advanced %s", "発展%s"),
            HTLangPatternProvider("Industrial %s", "工業用%s"),
        )
    }

    inline fun <R> map(transform: (T) -> R): HTTieredContents<R> = HTTieredContents(
        transform(basic),
        transform(advanced),
        transform(industrial),
    )

    inline fun <U, R> zip(other: HTTieredContents<U>, transform: (T, U) -> R): HTTieredContents<R> = HTTieredContents(
        transform(basic, other.basic),
        transform(advanced, other.advanced),
        transform(industrial, other.industrial),
    )

    override val size: Int get() = 3

    override fun iterator(): Iterator<T> = iterator {
        yield(basic)
        yield(advanced)
        yield(industrial)
    }
}
