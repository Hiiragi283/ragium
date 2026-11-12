package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.HTBasicEntityBlock
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

open class HTFluidStorageBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTBasicEntityBlock(type, properties) {
    override fun useItemOn(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (itemStack.isEmpty) return InteractionResult.TRY_WITH_EMPTY_HAND
        if (level is ServerLevel) {
            if (!player.isShiftKeyDown) {
                val tank: HTFluidTank =
                    level.getTypedBlockEntity<HTBaseTankBlockEntity>(pos)?.tank ?: return InteractionResult.FAIL
                if (interactTank(itemStack, state, level, pos, player, hand, hitResult, tank)) {
                    return InteractionResult.SUCCESS_SERVER
                }
            }
            return InteractionResult.PASS
        } else {
            return InteractionResult.CONSUME
        }
    }

    protected open fun interactTank(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
        tank: HTFluidTank,
    ): Boolean = FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction, null)
}
