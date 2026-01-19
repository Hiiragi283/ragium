package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.core.common.block.HTBlockWithModularUI
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.FluidUtil

class HTTankBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTBasicEntityBlock(type, properties),
    HTBlockWithDescription,
    HTBlockWithModularUI {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
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
        val result: ItemInteractionResult = super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        if (stack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            val tankEntity: HTTankBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.FAIL
            val moved: Boolean = FluidUtil.interactWithFluidHandler(player, hand, tankEntity)
            if (moved) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }
        return result
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun getDescription(): HTTranslation = RagiumTranslation.TANK
}
