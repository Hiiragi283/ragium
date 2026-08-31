package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTSimpleKeyOrValue
import hiiragi283.lib.util.Ior
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * この[Holder][this]から[ResourceKey]を取得します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.key ?: error("Unregistered holder: $this")

/**
 * この[Holder][this]を[HTSimpleKeyOrValue]に変換します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException この[Holder][this]が[HTDeferredHolder]または[Holder.Reference]でない場合
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun <R : Any> Holder<R>.asKeyOrValue(): HTSimpleKeyOrValue<R> = when (this) {
    is HTDeferredHolder<R, *> -> this
    is Holder.Reference<R> -> HTSimpleKeyOrValue {
        this.runCatching(Holder.Reference<R>::value).fold(
            { value: R -> this.key?.let { Ior.Both(it, value) } ?: Ior.Right(value) },
            { it -> this.key?.let { Ior.Left(it) } ?: throw it },
        )
    }
    else -> error("Cannot convert direct holder $this to HTSimpleKeyOrValue")
}

/**
 * この[DeferredHolder][this]を[HTDeferredHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun <R : Any, T : R> DeferredHolder<R, T>.asKeyOrValue(): HTDeferredHolder<R, T> = when (this) {
    is HTDeferredHolder<R, T> -> this
    else -> HTDeferredHolder(this.key)
}

/**
 * この[TypedInstance][this]から[ResourceKey]を取得します。
 * @param T 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> TypedInstance<T>.getKeyOrThrow(): ResourceKey<T> = this.typeHolder().getKeyOrThrow()

//    Block    //

/**
 * この[Holder][this]が空かどうか判定します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val Holder<out ItemLike>.isAir: Boolean get() = this.`is`(HTConstants.Keys.AIR)

//    Fluid    //

/**
 * この[Holder][this]が空かどうか判定します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val Holder<Fluid>.isEmpty: Boolean get() = this.`is`(HTConstants.Keys.EMPTY)
