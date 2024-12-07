package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTRegistryEntryList
import hiiragi283.ragium.api.extension.isIn
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.BiPredicate

interface HTMultiblockPattern : BiPredicate<World, BlockPos> {
    val text: MutableText

    fun getPreviewState(world: World): BlockState?

    companion object {
        @JvmStatic
        fun of(block: Block): HTMultiblockPattern = EntryListImpl(HTRegistryEntryList.of(block))

        @JvmStatic
        fun of(content: HTContent<Block>): HTMultiblockPattern = of(content.value)

        @JvmStatic
        fun tag(tagKey: TagKey<Block>): HTMultiblockPattern = EntryListImpl(HTRegistryEntryList.ofTag(tagKey, Registries.BLOCK))
    }

    //    Impl    //

    private class EntryListImpl(val entryList: HTRegistryEntryList<Block>) : HTMultiblockPattern {
        override val text: MutableText = entryList.getText(Block::getName)

        override fun getPreviewState(world: World): BlockState? = when (entryList.size) {
            0 -> null
            1 -> entryList[0].defaultState
            else -> entryList[((world.time % (20 * entryList.size)) / 20).toInt()].defaultState
        }

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isIn(entryList)
    }
}
