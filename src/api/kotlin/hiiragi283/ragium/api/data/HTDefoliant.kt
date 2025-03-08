package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.emptyBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

@ConsistentCopyVisibility
data class HTDefoliant private constructor(val state: BlockState) {
    companion object {
        @JvmField
        val CODEC: Codec<HTDefoliant> = BlockState.CODEC.xmap(::HTDefoliant, HTDefoliant::state)

        @JvmField
        val AIR = HTDefoliant(Blocks.AIR.defaultBlockState())

        @JvmStatic
        fun of(state: BlockState): HTDefoliant = if (state.isAir) AIR else HTDefoliant(state)

        @JvmStatic
        fun of(block: Block): HTDefoliant = of(block.defaultBlockState())
    }

    fun updateState(level: Level, pos: BlockPos) {
        if (state.isAir) {
            level.emptyBlock(pos)
        } else {
            level.setBlockAndUpdate(pos, state)
        }
    }
}
