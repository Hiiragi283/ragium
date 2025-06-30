package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult

@Suppress("DEPRECATION")
abstract class HTFeastBlock(properties: Properties, val hasLeftovers: Boolean) : Block(properties) {
    companion object {
        @JvmField
        val HORIZONTAL: DirectionProperty = HTBlockStateProperties.HORIZONTAL
    }

    init {
        registerDefaultState(
            stateDefinition
                .any()
                .setValue(HORIZONTAL, Direction.NORTH)
                .setValue(getServingsProperty(), getMaxServings()),
        )
    }

    abstract fun getServingsProperty(): IntegerProperty

    abstract fun getMaxServings(): Int

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState = if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
        Blocks.AIR.defaultBlockState()
    } else {
        super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val servings: Int = state.getValue(getServingsProperty())
        if (servings == 0) {
            level.destroyBlock(pos, true)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        } else {
            val servingStack: ItemStack = getServingItem(state)
            val heldStack: ItemStack = player.getItemInHand(hand)
            if (servings > 0) {
                val bool1: Boolean = !servingStack.hasCraftingRemainingItem()
                val bool2: Boolean = ItemStack.isSameItem(heldStack, servingStack.craftingRemainingItem)
                if (bool1 || bool2) {
                    level.setBlockAndUpdate(pos, state.setValue(getServingsProperty(), servings - 1))
                    heldStack.consume(1, player)
                    dropStackAt(player, servingStack)
                    if (level.getBlockState(pos).getValue(getServingsProperty()) == 0 && !hasLeftovers) {
                        level.removeBlock(pos, false)
                    }
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1f, 1f)
                    return ItemInteractionResult.sidedSuccess(level.isClientSide)
                }
            }
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        }
    }

    abstract fun getServingItem(state: BlockState): ItemStack

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean = level.getBlockState(pos.below()).isSolid

    final override fun getStateForPlacement(context: BlockPlaceContext): BlockState? =
        defaultBlockState().setValue(HORIZONTAL, context.horizontalDirection.opposite)

    final override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HORIZONTAL, getServingsProperty())
    }

    final override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    final override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int = state.getValue(getServingsProperty())

    final override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false
}
