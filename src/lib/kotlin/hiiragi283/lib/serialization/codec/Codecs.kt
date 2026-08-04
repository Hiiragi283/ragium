package hiiragi283.lib.serialization.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.util.DFUEither
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.java
import hiiragi283.lib.util.kotlin
import hiiragi283.lib.util.left
import hiiragi283.lib.util.right
import hiiragi283.lib.util.unwrap
import java.util.Optional

//    List    //

/**
 * この[Codec][this]を[List]の[Codec]に変換します。
 * @param range リストの[長さ][List.size]の範囲
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<A>.listOf(range: IntRange): Codec<List<A>> = this.listOf(range.first, range.last)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = HTCodecs.either(this.listOf(), this).xmap(
    { either: Either<List<A>, A> -> either.map(::listOf).unwrap() },
    { list: List<A> -> list.singleOrNull()?.right() ?: list.left() },
)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param range リストの[長さ][List.size]の範囲
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<A>.listOrElement(range: IntRange): Codec<List<A>> = this.listOrElement(range.first, range.last)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param min リストの[長さ][List.size]の最小値
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> = HTCodecs.either(this.listOf(min, max), this).xmap(
    { either: Either<List<A>, A> -> either.map(::listOf).unwrap() },
    { list: List<A> -> list.singleOrNull()?.right() ?: list.left() },
)

//    Set    //

/**
 * この[Codec][this]を[Set]の[Codec]に変換します。
 * @return [Set]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<List<A>>.setOf(): Codec<Set<A>> = this.xmap(List<A>::toSet, Set<A>::toList)

//    Either    //

@JvmName("convertToEither")
fun <A, B> MapCodec<DFUEither<A, B>>.convert(): MapCodec<Either<A, B>> = this.xmap({ it.kotlin }, { it.java })

//    Option    //

@JvmName("convertToOption")
fun <A : Any> MapCodec<Optional<A>>.convert(): MapCodec<Option<A>> = this.xmap({ it.kotlin }, { it.java })
