package hiiragi283.lib.collection

/**
 * @param E リストの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <E> nelOf(head: E, vararg tail: E): Nel<E> = Nel(head, listOf(*tail))

/**
 * @param E リストの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <E> E.toNel(): Nel<E> = NonEmptyList(this)

//    Iterable <-> Nel    //

/**
 * この[Iterable][this]を[Nel]に変換します。
 * @param E リストの要素のクラス
 * @throws NoSuchElementException [Iterable.none]の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <E> Iterable<E>.toNel(): Nel<E> = NonEmptyList(this.first(), this.drop(1))

/**
 * この[Iterable][this]を[Nel]に変換します。
 * @param E リストの要素のクラス
 * @return [Iterable.none]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <E> Iterable<E>.toNelOrNull(): Nel<E>? {
    val head: E = this.firstOrNull() ?: return null
    return NonEmptyList(head, this.drop(1))
}
