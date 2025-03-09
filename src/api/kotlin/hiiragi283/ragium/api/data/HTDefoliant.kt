package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.emptyBlock
import net.minecraft.core.BlockPos
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.*

@ConsistentCopyVisibility
data class HTDefoliant private constructor(val state: BlockState) {
    companion object {
        @JvmStatic
        private val RAW_CODEC: Codec<HTDefoliant> = BlockState.CODEC.xmap(::of, HTDefoliant::state)

        @JvmField
        val CODEC: Codec<HTDefoliant> = ExtraCodecs
            .optionalEmptyMap(RAW_CODEC)
            .xmap(
                { optional: Optional<HTDefoliant> -> optional.orElse(EMPTY) },
                { defoliant: HTDefoliant -> if (defoliant.state.isAir) Optional.empty() else Optional.of(defoliant) },
            )

        @JvmField
        val EMPTY = HTDefoliant(Blocks.AIR.defaultBlockState())

        @JvmStatic
        fun of(state: BlockState): HTDefoliant = if (state.isAir) EMPTY else HTDefoliant(state)

        @JvmStatic
        fun of(block: Block): HTDefoliant = of(block.defaultBlockState())
    }

    fun updateState(level: Level, pos: BlockPos) {
        if (level.isEmptyBlock(pos)) return
        if (state.isAir) {
            level.emptyBlock(pos)
        } else {
            level.setBlockAndUpdate(pos, state)
        }
    }
}
