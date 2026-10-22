package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.references.BlockItemId
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidIds

/**
 * この[Holder][this]から[ResourceKey]を取得します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.keyOrNull() ?: error("Unregistered holder: $this")

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

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
@JvmName("isBlockOf")
fun Holder<Block>.isOf(id: BlockItemId): Boolean = this.`is`(id.block())

//    Fluid    //

/**
 * この[Holder][this]が空かどうか判定します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val Holder<Fluid>.isEmpty: Boolean get() = this.`is`(FluidIds.EMPTY)

//    Item    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
@JvmName("isItemOf")
fun Holder<Item>.isOf(id: BlockItemId): Boolean = this.`is`(id.item())
