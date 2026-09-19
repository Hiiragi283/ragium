package hiiragi283.lib.serialization.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.identity
import hiiragi283.lib.util.unwrap
import net.minecraft.util.ExtraCodecs

//    List    //

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = ExtraCodecs.compactListCodec(this)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param min リストの[長さ][List.size]の最小値
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> =
    ExtraCodecs.compactListCodec(this, this.listOf(min, max))

/**
 * この[Codec][this]を[List]の[MapCodec]に変換します。
 * @param singleName 要素が一つの場合のキーの名前
 * @param listName 要素数が0または2以上の場合の名前
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun <A : Any> Codec<A>.compactListFieldOf(singleName: String, listName: String): MapCodec<List<A>> = HTCodecs
    .mapEither(this.listOf().fieldOf(listName), this.fieldOf(singleName))
    .xmap(
        { either: Either<List<A>, A> -> either.map(::listOf).unwrap() },
        { list: List<A> -> list.singleOrNull()?.let { Either.Right(it) } ?: Either.Left(list) }
    )

/**
 * この[Codec][this]を[List]の[MapCodec]に変換します。
 * @param singleName 要素が一つの場合のキーの名前
 * @param listName 要素数が0または2以上の場合の名前
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun <A : Any> Codec<A>.compactListFieldOf(singleName: String, listName: String, min: Int, max: Int): MapCodec<List<A>> =
    HTCodecs
        .mapEither(this.listOf(min, max).fieldOf(listName), this.fieldOf(singleName))
        .xmap(
            { either: Either<List<A>, A> -> either.map(::listOf).unwrap() },
            { list: List<A> -> list.singleOrNull()?.let { Either.Right(it) } ?: Either.Left(list) }
        )

//    NonEmptyList    //

/**
 * この[Codec][this]を[Nel]の[Codec]に変換します。
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <A : Any> Codec<A>.nelOf(max: Int = Int.MAX_VALUE): Codec<Nel<A>> =
    this.listOf(1, max).xmap(List<A>::toNel, identity())

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[Nel]の[Codec]に変換します。
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
fun <A : Any> Codec<A>.nelOrElement(max: Int = Int.MAX_VALUE): Codec<Nel<A>> =
    this.listOrElement(1, max).xmap(List<A>::toNel, identity())

/**
 * この[Codec][this]を[Nel]の[MapCodec]に変換します。
 * @param singleName 要素が一つの場合のキーの名前
 * @param nelName 要素数が0または2以上の場合の名前
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun <A : Any> Codec<A>.compactNelFieldOf(
    singleName: String,
    nelName: String,
    max: Int = Int.MAX_VALUE
): MapCodec<Nel<A>> = HTCodecs
    .mapEither(this.nelOf(max).fieldOf(nelName), this.fieldOf(singleName))
    .xmap(
        { either: Either<Nel<A>, A> -> either.map(::nelOf).unwrap() },
        { list: Nel<A> -> list.singleOrNull()?.let { Either.Right(it) } ?: Either.Left(list) }
    )
