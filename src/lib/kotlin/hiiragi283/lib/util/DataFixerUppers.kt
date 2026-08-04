package hiiragi283.lib.util

//    DFUEither <-> Either    //

/**
 * DataFixerUpper由来の[Either][com.mojang.datafixers.util.Either]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias DFUEither<A, B> = com.mojang.datafixers.util.Either<A, B>

/**
 * [DFUEither]を[Either]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <A, B> DFUEither<A, B>.kotlin: Either<A, B> get() = this.map({ Either.Left(it) }, { Either.Right(it) })

/**
 * [Either]を[DFUEither]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <A, B> Either<A, B>.java: DFUEither<A, B> get() = this.fold({ DFUEither.left(it) }, { DFUEither.right(it) })

//    DFUPair <-> Pair    //

/**
 * DataFixerUpper由来の[Either][com.mojang.datafixers.util.Pair]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias DFUPair<A, B> = com.mojang.datafixers.util.Pair<A, B>

/**
 * [DFUPair]を[Pair]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <A, B> DFUPair<A, B>.kotlin: Pair<A, B> get() = this.first to this.second

/**
 * [Pair]を[DFUPair]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <A, B> Pair<A, B>.java: DFUPair<A, B> get() = DFUPair.of(this.first, this.second)
