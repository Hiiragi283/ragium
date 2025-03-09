package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.*

@ConsistentCopyVisibility
data class HTNapalm private constructor(val state: BlockState) {
    companion object {
        @JvmStatic
        private val RAW_CODEC: Codec<HTNapalm> = BlockState.CODEC.xmap(::of, HTNapalm::state)

        @JvmField
        val CODEC: Codec<HTNapalm> = ExtraCodecs.optionalEmptyMap(RAW_CODEC)
            .xmap(
                { optional: Optional<HTNapalm> -> optional.orElse(EMPTY) },
                { napalm: HTNapalm -> if (napalm.state.`is`(Blocks.FIRE)) Optional.empty() else Optional.of(napalm) }
            )

        @JvmField
        val EMPTY = HTNapalm(Blocks.FIRE.defaultBlockState())

        @JvmStatic
        fun of(state: BlockState): HTNapalm = if (state.`is`(Blocks.FIRE)) EMPTY else HTNapalm(state)

        @JvmStatic
        fun of(block: Block): HTNapalm = of(block.defaultBlockState())
    }

    fun updateState(level: Level, pos: BlockPos) {
        if (this == EMPTY) {
            if (!state.canSurvive(level, pos)) return
        }
        level.setBlockAndUpdate(pos, state)
    }
}
