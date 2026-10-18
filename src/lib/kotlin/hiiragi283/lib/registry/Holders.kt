package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.TypedInstance
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.holdersets.AndHolderSet
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * この[Holder][this]から[ResourceKey]を取得します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.key ?: error("Unregistered holder: $this")

/**
 * この[TypedInstance][this]から[ResourceKey]を取得します。
 * @param T 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> TypedInstance<T>.getKeyOrThrow(): ResourceKey<T> = this.typeHolder().getKeyOrThrow()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
infix fun <T : Any> HolderSet<T>.and(other: HolderSet<T>): HolderSet<T> = AndHolderSet(this, other)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
infix fun <T : Any> HolderSet<T>.or(other: HolderSet<T>): HolderSet<T> = OrHolderSet(this, other)

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
