package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.resource.BlockItemKey
import hiiragi283.lib.resource.HTBlockItemWithKey
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.HTHasTranslationKey
import net.minecraft.resources.Identifier
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
data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(
    override val block: HTDeferredBlock<BLOCK>,
    override val item: HTDeferredItem<ITEM>
) : HTBlockItemWithKey<BLOCK, ITEM>,
    HTHasTranslationKey by item,
    HTHasText by item,
    ItemLike by item,
    HTItemInstanceLike by item {
    /**
     * @since 26.1.4
     */
    constructor(key: BlockItemKey) : this(HTDeferredBlock(key.block), HTDeferredItem(key.item))

    constructor(id: Identifier) : this(HTDeferredBlock(id), HTDeferredItem(id))
}
