package hiiragi283.ragium.common.block

import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CakeBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult

/**
 * @see CakeBlock
 */
class HTMeatBlock(private val food: FoodProperties, properties: Properties) : Block(properties) {
    companion object {
        @JvmField
        val BITES: IntegerProperty = CakeBlock.BITES
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(BITES, 0))
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS
            }
            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty) {
                return InteractionResult.CONSUME
            }
        }
        return eat(level, pos, state, player)
    }

    private fun eat(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        player: Player,
    ): InteractionResult {
        if (!player.canEat(false)) return InteractionResult.PASS
        player.foodData.eat(food)
        val bites: Int = state.getValue(BITES)
        level.gameEvent(player, GameEvent.EAT, pos)
        if (bites < 6) {
            level.setBlockAndUpdate(pos, state.setValue(BITES, bites + 1))
        } else {
            level.removeBlock(pos, false)
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos)
            HTItemDropHelper.giveStackTo(player, ItemStack(Items.BONE))
        }
        return InteractionResult.SUCCESS
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BITES)
    }

    override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int = (7 - state.getValue(BITES)) * 2

    override fun hasAnalogOutputSignal(state: BlockState): Boolean = true
}
