package hiiragi283.ragium.common.util

sealed interface BothEither<L : Any, R : Any> {
    companion object {
        @JvmStatic
        fun <L : Any, R : Any> left(left: L): BothEither<L, R> = Left(left)

        @JvmStatic
        fun <L : Any, R : Any> right(right: R): BothEither<L, R> = Right(right)

        @JvmStatic
        fun <L : Any, R : Any> both(left: L, right: R): BothEither<L, R> = Both(left, right)
    }

    fun getLeft(): L?

    fun getRight(): R?

    fun getBoth(): Pair<L, R>?

    fun ifLeft(action: (L) -> Unit): BothEither<L, R>

    fun ifRight(action: (R) -> Unit): BothEither<L, R>

    fun ifBoth(leftAction: (L) -> Unit, rightAction: (R) -> Unit, flag: Boolean): BothEither<L, R>

    fun <T : Any> leftMap(leftMapper: (L) -> T): BothEither<T, R>

    fun <T : Any> rightMap(rightMapper: (R) -> T): BothEither<L, T>

    fun <T : Any> getMapped(leftMapper: (L) -> T, rightMapper: (R) -> T, flag: Boolean): T

    //    Left    //

    class Left<L : Any, R : Any>(
        @JvmField val left: L,
    ) : BothEither<L, R> {
        override fun getLeft(): L = left

        override fun getRight(): R? = null

        override fun getBoth(): Pair<L, R>? = null

        override fun ifLeft(action: (L) -> Unit): Left<L, R> = apply { action(left) }

        override fun ifRight(action: (R) -> Unit): Left<L, R> = this

        override fun ifBoth(leftAction: (L) -> Unit, rightAction: (R) -> Unit, flag: Boolean): BothEither<L, R> = apply { leftAction(left) }

        override fun <T : Any> leftMap(leftMapper: (L) -> T): BothEither<T, R> = Left(leftMapper(left))

        override fun <T : Any> rightMap(rightMapper: (R) -> T): BothEither<L, T> = Left(left)

        override fun <T : Any> getMapped(leftMapper: (L) -> T, rightMapper: (R) -> T, flag: Boolean): T = leftMapper(left)
    }

    //    Right    //

    class Right<L : Any, R : Any>(
        @JvmField val right: R,
    ) : BothEither<L, R> {
        override fun getLeft(): L? = null

        override fun getRight(): R = right

        override fun getBoth(): Pair<L, R>? = null

        override fun ifLeft(action: (L) -> Unit): Right<L, R> = this

        override fun ifRight(action: (R) -> Unit): Right<L, R> = apply { action(right) }

        override fun ifBoth(leftAction: (L) -> Unit, rightAction: (R) -> Unit, flag: Boolean): BothEither<L, R> =
            apply { rightAction(right) }

        override fun <T : Any> leftMap(leftMapper: (L) -> T): BothEither<T, R> = Right(right)

        override fun <T : Any> rightMap(rightMapper: (R) -> T): BothEither<L, T> = Right(rightMapper(right))

        override fun <T : Any> getMapped(leftMapper: (L) -> T, rightMapper: (R) -> T, flag: Boolean): T = rightMapper(right)
    }

    //    Both    //

    class Both<L : Any, R : Any>(
        @JvmField val left: L,
        @JvmField val right: R,
    ) : BothEither<L, R> {
        override fun getLeft(): L = left

        override fun getRight(): R = right

        override fun getBoth(): Pair<L, R> = left to right

        override fun ifLeft(action: (L) -> Unit): Both<L, R> = apply { action(left) }

        override fun ifRight(action: (R) -> Unit): Both<L, R> = apply { action(right) }

        override fun ifBoth(leftAction: (L) -> Unit, rightAction: (R) -> Unit, flag: Boolean): BothEither<L, R> = apply {
            when (flag) {
                true -> leftAction(left)
                false -> rightAction(right)
            }
        }

        override fun <T : Any> leftMap(leftMapper: (L) -> T): BothEither<T, R> = Left(leftMapper(left))

        override fun <T : Any> rightMap(rightMapper: (R) -> T): BothEither<L, T> = Right(rightMapper(right))

        override fun <T : Any> getMapped(leftMapper: (L) -> T, rightMapper: (R) -> T, flag: Boolean): T = when (flag) {
            true -> leftMapper(left)
            false -> rightMapper(right)
        }
    }
}
