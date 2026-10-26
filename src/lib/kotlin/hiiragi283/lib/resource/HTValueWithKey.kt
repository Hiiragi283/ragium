package hiiragi283.lib.resource

import com.mojang.serialization.DataResult
import hiiragi283.lib.util.getOrNull
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * シンプルな[HTValueWithKey]のエイリアスです。
 * @param R レジストリの値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
typealias HTSimpleValueWithKey<R> = HTValueWithKey<R, R>

/**
 * [ID][ResourceKey]または値を提供するインターフェースです。
 * @param R レジストリの値のクラス
 * @param T 提供する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTValueWithKey<R : Any, out T : R> {
    /**
     * [ID][ResourceKey]を取得します。
     * @throws IllegalStateException 戻り値が`null`の場合
     */
    fun key(): ResourceKey<R>

    /**
     * [ID][Identifier]を取得します。
     * @throws IllegalStateException 戻り値が`null`の場合
     */
    fun id(): Identifier = key().identifier()

    fun asResult(): DataResult<@UnsafeVariance T>

    /**
     * 値を取得します。
     */
    fun getOrNull(): T? = asResult().getOrNull()

    /**
     * 値を取得します。
     */
    fun getOrThrow(): T = asResult().getOrThrow()
}
