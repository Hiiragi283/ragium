package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.Tags
import kotlin.math.min

class HTExpBerriesBushBlock(properties: Properties) :
    BushBlock(properties),
    BonemealableBlock {
    companion object {
        @JvmField
        val AGE: IntegerProperty = BlockStateProperties.AGE_3

        const val MAX_AGE = 3
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(AGE, 0))
    }

    fun getAge(state: BlockState): Int = state.getValue(AGE)

    fun isMaxAge(state: BlockState): Boolean = getAge(state) >= MAX_AGE

    override fun codec(): MapCodec<out BushBlock> = throw UnsupportedOperationException()

    override fun isRandomlyTicking(state: BlockState): Boolean = !isMaxAge(state)

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        val age: Int = getAge(state)
        if (!isMaxAge(state) && CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            val newState: BlockState = state.setValue(AGE, age + 1)
            level.setBlock(pos, newState, 2)
            CommonHooks.fireCropGrowPost(level, pos, state)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState))
        }
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = when {
        !isMaxAge(state) && stack.`is`(Tags.Items.FERTILIZERS) -> ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        else -> super.useItemOn(stack, state, level, pos, player, hand, hitResult)
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        val isMaxAge: Boolean = isMaxAge(state)
        if (getAge(state) > 1) {
            val count: Int = 1 + level.random.nextInt(2)
            dropStackAt(player, RagiumItems.EXP_BERRIES.toStack(count + if (isMaxAge) 1 else 0))
            level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS)
            val newState: BlockState = state.setValue(AGE, 1)
            level.setBlock(pos, newState, 2)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState))
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useWithoutItem(state, level, pos, player, hitResult)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    //    BonemealableBlock    //

    override fun isValidBonemealTarget(level: LevelReader, pos: BlockPos, state: BlockState): Boolean = !isMaxAge(state)

    override fun isBonemealSuccess(
        level: Level,
        random: RandomSource,
        pos: BlockPos,
        state: BlockState,
    ): Boolean = true

    override fun performBonemeal(
        level: ServerLevel,
        random: RandomSource,
        pos: BlockPos,
        state: BlockState,
    ) {
        val age: Int = min(3, getAge(state) + 1)
        val newState: BlockState = state.setValue(AGE, age)
        level.setBlock(pos, newState, 2)
    }
}
