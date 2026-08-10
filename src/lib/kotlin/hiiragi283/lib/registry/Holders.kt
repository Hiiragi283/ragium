package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.lib.resource.SimpleSupplierWithKey
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * この[Holder][this]から[ResourceKey]を取得します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

/**
 * この[Holder][this]を[SimpleSupplierWithKey]に変換します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.kind]が[Holder.Kind.DIRECT]の場合
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <R : Any> Holder<R>.toLike(): SimpleSupplierWithKey<R> = when (this.kind()) {
    Holder.Kind.REFERENCE -> HolderWithKey(this)
    Holder.Kind.DIRECT -> error("Cannot convert direct holder to SimpleSupplierWithKey")
}

@JvmRecord
private data class HolderWithKey<R : Any>(private val holder: Holder<R>) : SimpleSupplierWithKey<R> {
    override fun get(): R = holder.value()

    override fun getKey(): ResourceKey<R> = holder.getKeyOrThrow()
}

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTDeferredHolder<R, T> = when (this) {
    is HTDeferredHolder<R, T> -> this
    else -> HTDeferredHolder(this.key)
}

//    Block    //

val Holder<out ItemLike>.isAir: Boolean get() = this.`is`(HTConstants.Keys.AIR)

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun Holder<Block>.toBlockLike(): SimpleBlockItemSupplierWithKey = when (this.kind()) {
    Holder.Kind.REFERENCE -> BlockHolderWithKey(this)
    Holder.Kind.DIRECT -> error("Cannot convert direct holder to SimpleBlockItemSupplierWithKey")
}

@JvmRecord
private data class BlockHolderWithKey(override val block: SimpleSupplierWithKey<Block>, override val item: SimpleSupplierWithKey<Item>) : SimpleBlockItemSupplierWithKey {
    constructor(holder: Holder<Block>) : this(
        holder.toLike(),
        object : SimpleSupplierWithKey<Item> {
            override fun get(): Item = holder.value().asItem()

            @Suppress("DEPRECATION")
            override fun getKey(): ResourceKey<Item> = get().builtInRegistryHolder().getKeyOrThrow()
        },
    )
}

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <BLOCK : Block> DeferredHolder<Block, BLOCK>.toBlockLike(): HTDeferredBlockAndItem<BLOCK, Item> = HTDeferredBlockAndItem(this.id)

//    Fluid    //

val Holder<Fluid>.isEmpty: Boolean get() = this.`is`(HTConstants.Keys.EMPTY)
