package hiiragi283.lib.color

import hiiragi283.lib.data.lang.HTLangName
import net.minecraft.world.item.DyeColor

/**
 * 色のバリエーションを持つ要素をまとめるクラスです。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTColoredCollection<out T>(
    val white: T,
    val orange: T,
    val magenta: T,
    val lightBlue: T,
    val yellow: T,
    val lime: T,
    val pink: T,
    val gray: T,
    val lightGray: T,
    val cyan: T,
    val purple: T,
    val blue: T,
    val brown: T,
    val green: T,
    val red: T,
    val black: T
) : AbstractCollection<T>() {
    companion object {
        @JvmField
        val VALUES: HTColoredCollection<DyeColor> = HTColoredCollection(
            DyeColor.WHITE,
            DyeColor.ORANGE,
            DyeColor.MAGENTA,
            DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.PINK,
            DyeColor.GRAY,
            DyeColor.LIGHT_GRAY,
            DyeColor.CYAN,
            DyeColor.PURPLE,
            DyeColor.BLUE,
            DyeColor.BROWN,
            DyeColor.GREEN,
            DyeColor.RED,
            DyeColor.BLACK
        )

        @JvmField
        val TRANSLATED_NAMES: HTColoredCollection<HTLangName> = HTColoredCollection(
            HTLangName("White", "白色"),
            HTLangName("Orange", "橙色"),
            HTLangName("Magenta", "赤紫色"),
            HTLangName("Light Blue", "空色"),
            HTLangName("Yellow", "黄色"),
            HTLangName("Lime", "黄緑色"),
            HTLangName("Pink", "桃色"),
            HTLangName("Gray", "灰色"),
            HTLangName("Light Gray", "薄灰色"),
            HTLangName("Cyan", "青緑色"),
            HTLangName("Purple", "紫色"),
            HTLangName("Blue", "青色"),
            HTLangName("Brown", "茶色"),
            HTLangName("Green", "緑色"),
            HTLangName("Red", "赤色"),
            HTLangName("Black", "黒色")
        )
    }

    operator fun get(color: DyeColor): T = when (color) {
        DyeColor.WHITE -> white
        DyeColor.ORANGE -> orange
        DyeColor.MAGENTA -> magenta
        DyeColor.LIGHT_BLUE -> lightBlue
        DyeColor.YELLOW -> yellow
        DyeColor.LIME -> lime
        DyeColor.PINK -> pink
        DyeColor.GRAY -> gray
        DyeColor.LIGHT_GRAY -> lightGray
        DyeColor.CYAN -> cyan
        DyeColor.PURPLE -> purple
        DyeColor.BLUE -> blue
        DyeColor.BROWN -> brown
        DyeColor.GREEN -> green
        DyeColor.RED -> red
        DyeColor.BLACK -> black
    }

    fun asSequence(): Sequence<T> = DyeColor.entries.asSequence().map(::get)

    override val size: Int = 16

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = asSequence().iterator()

    inline fun <R> map(transform: (T) -> R): HTColoredCollection<R> = HTColoredCollection(
        transform(white),
        transform(orange),
        transform(magenta),
        transform(lightBlue),
        transform(yellow),
        transform(lime),
        transform(pink),
        transform(gray),
        transform(lightGray),
        transform(cyan),
        transform(purple),
        transform(blue),
        transform(brown),
        transform(green),
        transform(red),
        transform(black)
    )

    inline fun <U, R> zip(other: HTColoredCollection<U>, transform: (T, U) -> R): HTColoredCollection<R> =
        HTColoredCollection(
            transform(white, other.white),
            transform(orange, other.orange),
            transform(magenta, other.magenta),
            transform(lightBlue, other.lightBlue),
            transform(yellow, other.yellow),
            transform(lime, other.lime),
            transform(pink, other.pink),
            transform(gray, other.gray),
            transform(lightGray, other.lightGray),
            transform(cyan, other.cyan),
            transform(purple, other.purple),
            transform(blue, other.blue),
            transform(brown, other.brown),
            transform(green, other.green),
            transform(red, other.red),
            transform(black, other.black)
        )
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <T> HTColoredCollection(init: (color: DyeColor) -> T): HTColoredCollection<T> =
    HTColoredCollection.VALUES.map(init)
