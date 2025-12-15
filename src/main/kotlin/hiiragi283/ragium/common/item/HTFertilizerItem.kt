package hiiragi283.ragium.common.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see blusunrize.immersiveengineering.common.items.FertilizerItem
 */
class HTFertilizerItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val side: Direction = context.clickedFace
        val offsetPos: BlockPos = pos.relative(side)
        val stack: ItemStack = context.itemInHand
        if (BoneMealItem.applyBonemeal(stack, level, pos, context.player)) {
            if (level.isClientSide) {
                BoneMealItem.addGrowthParticles(level, pos, 100)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        } else {
            val state: BlockState = level.getBlockState(pos)
            val isSturdy: Boolean = state.isFaceSturdy(level, pos, side)
            if (isSturdy && BoneMealItem.growWaterPlant(stack, level, offsetPos, side)) {
                if (level.isClientSide) {
                    BoneMealItem.addGrowthParticles(level, pos, 100)
                }
                return InteractionResult.sidedSuccess(level.isClientSide)
            }
        }
        return super.useOn(context)
    }
}
