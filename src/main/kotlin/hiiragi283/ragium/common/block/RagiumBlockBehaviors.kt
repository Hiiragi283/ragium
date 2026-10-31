package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

data object RagiumBlockBehaviors {
    @JvmStatic
    fun interactWithTank(
        stack: ItemStack,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (stack.isEmpty) return InteractionResult.TRY_WITH_EMPTY_HAND
        if (!player.isShiftKeyDown) {
            if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction, null)) {
                return InteractionResult.SUCCESS
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND
    }
}
