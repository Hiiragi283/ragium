package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BeetrootBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

abstract class HTCropBlock(properties: Properties) : CropBlock(properties) {
    companion object {
        @JvmField
        val AGE: IntegerProperty = BeetrootBlock.AGE

        @JvmField
        val SHAPE_BY_AGE: Array<VoxelShape> = arrayOf<VoxelShape>(
            box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
        )
    }

    override fun getAgeProperty(): IntegerProperty = AGE

    override fun getMaxAge(): Int = 3

    abstract override fun getBaseSeedId(): ItemLike

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(AGE)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE_BY_AGE[getAge(state)]

    /**
     * @see de.ellpeck.actuallyadditions.mod.blocks.base.AACrops.useItemOn
     */
    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        // 最大まで成長していなければパス
        if (!isMaxAge(state)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        }
        // サーバー側のみで処理を行う
        if (!level.isClientSide) {
            val drops: List<ItemStack> = getDrops(state, level as ServerLevel, pos, null, player, stack)
            var droppedSeed = false
            for (drop: ItemStack in drops) {
                if (drop.isEmpty) continue
                if (drop.item == baseSeedId && !droppedSeed) {
                    drop.shrink(1)
                    droppedSeed = true
                }
                if (!drop.isEmpty) {
                    HTItemDropHelper.giveStackTo(player, drop)
                }
            }
            level.setBlockAndUpdate(pos, defaultBlockState().setValue(ageProperty, 0))
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }
}
