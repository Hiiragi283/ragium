package hiiragi283.ragium.common.block.storage

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.transfer.VoidingResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.FluidUtil

class HTVoidTankBlock(properties: Properties) : Block(properties) {
    companion object {
        @JvmField
        val VOIDING_HANDLER: VoidingResourceHandler<FluidResource> = VoidingResourceHandler(FluidResource.EMPTY)
    }

    override fun useItemOn(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): InteractionResult {
        val result: InteractionResult = super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
        if (itemStack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            val moved: Boolean = FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction, null)
            if (moved) {
                return InteractionResult.SUCCESS
            }
        }
        return result
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        HTTankBlock.SHAPE
}
