package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.resource.BlockItemSupplierWithKey
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithKey
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTBasicDeferredBlockAndItem]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

/**
 * [BlockItem]に基づいた[HTDeferredBlockAndItem]のエイリアスです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, BlockItem>

/**
 * [ブロック][Block]と[アイテム][Item]の両方をもつ[HTDeferredHolder]の補助クラスです。
 * @param BLOCK ブロックのクラス
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(override val block: HTDeferredBlock<BLOCK>, override val item: HTDeferredItem<ITEM>) :
    SupplierWithKey<Block, BLOCK>,
    BlockItemSupplierWithKey<BLOCK, ITEM>,
    HTIdLike.Translatable by item,
    ItemLike by item,
    HTItemInstanceLike by item {
    constructor(key: ResourceKey<Block>) : this(HTDeferredBlock(key), HTDeferredItem(key.identifier()))

    constructor(id: Identifier) : this(HTDeferredBlock(id), HTDeferredItem(id))

    override fun get(): BLOCK = block.get()

    override fun getKey(): ResourceKey<Block> = block.key
}
