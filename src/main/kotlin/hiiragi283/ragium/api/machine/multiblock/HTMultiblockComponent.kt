package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.content.HTContent
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.World
import java.util.function.Predicate

sealed interface HTMultiblockComponent : Predicate<BlockState> {
    fun getPreviewState(world: World): BlockState?

    data object Empty : HTMultiblockComponent {
        override fun getPreviewState(world: World): BlockState? = null

        override fun test(state: BlockState): Boolean = true
    }

    data class Simple(val block: Block) : HTMultiblockComponent {
        constructor(content: HTContent<Block>) : this(content.value)

        override fun getPreviewState(world: World): BlockState = block.defaultState

        override fun test(state: BlockState): Boolean = state.isOf(block)
    }

    data class State(val state: BlockState) : HTMultiblockComponent {
        override fun getPreviewState(world: World): BlockState = state

        override fun test(state: BlockState): Boolean = state == this.state
    }

    data class Tag(val entryList: RegistryEntryList<Block>) : HTMultiblockComponent {
        constructor(tagKey: TagKey<Block>) : this(Registries.BLOCK.getOrCreateEntryList(tagKey))

        override fun getPreviewState(world: World): BlockState = entryList.get(getIndex(world, entryList.size())).value().defaultState

        private fun getIndex(world: World, size: Int): Int = when (size) {
            0 -> 0
            1 -> 0
            else -> ((world.time % (20 * size)) / 20).toInt()
        }

        override fun test(state: BlockState): Boolean = state.isIn(entryList)
    }
}
