package hiiragi283.lib.registry

import hiiragi283.lib.data.tag.HTTagBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * Ragiumで使用される，建材ブロックをまとめて管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTDecorationContent(
    val base: HTSimpleDeferredBlockAndItem,
    val slab: HTBasicDeferredBlockAndItem<SlabBlock>,
    val stairs: HTBasicDeferredBlockAndItem<StairBlock>?,
    val wall: HTBasicDeferredBlockAndItem<WallBlock>?,
    val parent: () -> HTDecorationContent?
) {
    companion object {
        @JvmStatic
        fun copyOf(key: ResourceKey<Block>, base: HTSimpleDeferredBlockAndItem): BlockBehaviour.Properties =
            BlockBehaviour.Properties.ofFullCopy(base.getOrThrow()).setId(key)

        @JvmStatic
        fun create(
            register: HTDeferredBlockAndItemRegister,
            name: String,
            base: HTSimpleDeferredBlockAndItem,
            hasStairs: Boolean = true,
            hasWall: Boolean = true,
            parent: () -> HTDecorationContent? = { null }
        ): HTDecorationContent = HTDecorationContent(
            base,
            register.registerSimple(
                "${name}_slab",
                blockFactory = { key: ResourceKey<Block> -> SlabBlock(copyOf(key, base)) }
            ),
            when (hasStairs) {
                true -> register.registerSimple(
                    "${name}_stairs",
                    blockFactory = { key: ResourceKey<Block> -> StairBlock(base.defaultState, copyOf(key, base)) }
                )

                false -> null
            },
            when (hasWall) {
                true -> register.registerSimple(
                    "${name}_wall",
                    blockFactory = { key: ResourceKey<Block> -> WallBlock(copyOf(key, base)) }
                )

                false -> null
            },
            parent
        )
    }

    val all: List<HTSimpleDeferredBlockAndItem> get() = listOfNotNull(base, slab, stairs, wall)

    fun appendBlockTags(builder: (TagKey<Block>) -> HTTagBuilder<Block>) {
        builder(BlockTags.SLABS).add(slab)
        stairs?.let(builder(BlockTags.STAIRS)::add)
        wall?.let(builder(BlockTags.WALLS)::add)
    }

    fun appendItemTags(builder: (TagKey<Item>) -> HTTagBuilder<Item>) {
        builder(ItemTags.SLABS).add(slab.item)
        stairs?.item?.let(builder(ItemTags.STAIRS)::add)
        wall?.item?.let(builder(ItemTags.WALLS)::add)
    }
}
