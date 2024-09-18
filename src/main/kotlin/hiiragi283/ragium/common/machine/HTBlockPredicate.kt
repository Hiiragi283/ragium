package hiiragi283.ragium.common.machine

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*
import java.util.function.BiPredicate

sealed class HTBlockPredicate : BiPredicate<World, BlockPos> {
    abstract val previewStates: List<BlockState>

    abstract fun getPreviewState(world: World): BlockState?

    companion object {
        @JvmStatic
        fun any(): HTBlockPredicate = Any

        @JvmStatic
        fun block(block: Block): HTBlockPredicate = Simple(block)

        @JvmStatic
        fun state(state: BlockState): HTBlockPredicate = StateImpl(state)

        @JvmStatic
        fun tag(tagKey: TagKey<Block>): HTBlockPredicate = TagImpl(tagKey)
    }

    private data object Any : HTBlockPredicate() {
        override val previewStates: List<BlockState> = listOf()

        override fun test(world: World, pos: BlockPos): Boolean = true

        override fun getPreviewState(world: World): BlockState? = null
    }

    private data class Simple(private val block: Block) : HTBlockPredicate() {
        override val previewStates: List<BlockState> = listOf(block.defaultState)

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isOf(block)

        override fun getPreviewState(world: World): BlockState? = block.defaultState
    }

    private data class StateImpl(private val state: BlockState) : HTBlockPredicate() {
        override val previewStates: List<BlockState> = listOf(state)

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos) == state

        override fun getPreviewState(world: World): BlockState? = state
    }

    private data class TagImpl(private val tagKey: TagKey<Block>) : HTBlockPredicate() {
        override val previewStates: List<BlockState>
            get() =
                Registries.BLOCK
                    .iterateEntries(tagKey)
                    .map(RegistryEntry<Block>::value)
                    .map(Block::getDefaultState)

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isIn(tagKey)

        override fun getPreviewState(world: World): BlockState? = previewStates.getOrNull(getIndex(world))

        private fun getIndex(world: World): Int = when (val size: Int = previewStates.size) {
            0 -> 0
            1 -> 0
            else -> ((world.time % (20 * size)) / 20).toInt()
        }
    }
}
