package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.asText
import hiiragi283.ragium.api.extension.getEntryListOrEmpty
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.world.World
import java.util.function.Predicate

sealed interface HTMultiblockComponent : Predicate<BlockState> {
    val text: MutableText

    fun getPreviewState(world: World): BlockState?

    data class Simple(val block: Block) : HTMultiblockComponent {
        constructor(content: HTContent<Block>) : this(content.value)

        override val text: MutableText
            get() = block.name

        override fun getPreviewState(world: World): BlockState = block.defaultState

        override fun test(state: BlockState): Boolean = state.isOf(block)
    }

    data class Tag(val entryList: RegistryEntryList<Block>) : HTMultiblockComponent {
        constructor(tagKey: TagKey<Block>) : this(Registries.BLOCK.getEntryListOrEmpty(tagKey))

        override val text: MutableText
            get() = entryList.asText(Block::getName)

        override fun getPreviewState(world: World): BlockState = entryList.get(getIndex(world, entryList.size())).value().defaultState

        private fun getIndex(world: World, size: Int): Int = when (size) {
            0 -> 0
            1 -> 0
            else -> ((world.time % (20 * size)) / 20).toInt()
        }

        override fun test(state: BlockState): Boolean = state.isIn(entryList)
    }
}
