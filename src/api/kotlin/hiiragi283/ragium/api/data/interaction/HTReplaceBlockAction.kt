package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTReplaceBlockAction(private val state: BlockState, private val flag: Int) : HTBlockAction.ItemPreview {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTReplaceBlockAction> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    BlockState.CODEC.fieldOf("state").forGetter(HTReplaceBlockAction::state),
                    Codec.intRange(0, 63).optionalFieldOf("flag", 3).forGetter(HTReplaceBlockAction::flag),
                ).apply(instance, ::HTReplaceBlockAction)
        }

        @JvmStatic
        fun update(block: Block): HTReplaceBlockAction = update(block.defaultBlockState())

        @JvmStatic
        fun update(state: BlockState): HTReplaceBlockAction = HTReplaceBlockAction(state, Block.UPDATE_ALL)
    }

    override val codec: MapCodec<out HTBlockAction> = CODEC

    override fun applyAction(context: UseOnContext) {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val player: Player? = context.player
        if (player != null) {
            val oldState: BlockState = level.getBlockState(pos)
            oldState.block.playerWillDestroy(level, pos, oldState, player)
        }
        level.setBlock(pos, state, flag)
    }

    override fun getPreviewStack(): ItemStack = ItemStack(state.block)
}
