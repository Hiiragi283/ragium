package hiiragi283.ragium.common.shape

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*
import java.util.function.BiPredicate

sealed class HTBlockPredicate : BiPredicate<World, BlockPos> {
    protected abstract val previewStates: List<BlockState>

    private fun getIndex(world: World): Int = when (val size: Int = previewStates.size) {
        0 -> 0
        1 -> 0
        else -> ((world.time % (20 * size)) / 20).toInt()
    }

    fun getPreviewState(world: World): BlockState? = previewStates.getOrNull(getIndex(world))

    companion object {
        @JvmStatic
        fun any(): HTBlockPredicate = Any

        @JvmStatic
        fun block(vararg block: Block): HTBlockPredicate = Simple(*block)

        @JvmStatic
        fun tagKey(tagKey: TagKey<Block>): HTBlockPredicate = Group(tagKey)

        @JvmStatic
        fun states(states: List<BlockState>): HTBlockPredicate = States(states)
    }

    private data object Any : HTBlockPredicate() {
        override val previewStates: List<BlockState> = listOf(Blocks.AIR.defaultState)

        override fun test(world: World, pos: BlockPos): Boolean = true
    }

    private data class Simple(private val blocks: List<Block>) : HTBlockPredicate() {
        constructor(vararg blocks: Block) : this(blocks.toList())

        override val previewStates: List<BlockState> = blocks.map(Block::getDefaultState)

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).block in blocks
    }

    private data class Group(private val tagKey: TagKey<Block>) : HTBlockPredicate() {
        override val previewStates: List<BlockState>
            get() = Registries.BLOCK.iterateEntries(tagKey)
                .map(RegistryEntry<Block>::value)
                .map(Block::getDefaultState)

        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isIn(tagKey)
    }

    private data class States(override val previewStates: List<BlockState>) : HTBlockPredicate() {
        override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos) in previewStates
    }
}