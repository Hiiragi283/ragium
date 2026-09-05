package hiiragi283.lib.resource

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTBlockItemWithKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
typealias HTSimpleBlockItemWithKey = HTBlockItemWithKey<Block, Item>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
interface HTBlockItemWithKey<out BLOCK : Block, out ITEM : Item> {
    val block: HTKeyOrValue<Block, BLOCK>

    val item: HTKeyOrValue<Item, ITEM>

    val keyOrNull: BlockItemKey? get() = block.keyOrNull?.let { blockKey: ResourceKey<Block> ->
        item.keyOrNull?.let { itemKey: ResourceKey<Item> -> BlockItemKey(blockKey, itemKey) }
    }
    val keyOrThrow: BlockItemKey get() = BlockItemKey(block.keyOrThrow, item.keyOrThrow)
}
