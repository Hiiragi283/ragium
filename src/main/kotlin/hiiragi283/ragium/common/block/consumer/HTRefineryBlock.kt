package hiiragi283.ragium.common.block.consumer

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.extension.getTypedBlockEntity
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.block.entity.consumer.HTRefineryBlockEntity
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTRefineryBlock(type: HTEntityBlockType, properties: Properties) : HTTypedEntityBlock<HTEntityBlockType>(type, properties) {
    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (stack.isEmpty) return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        val refinery: HTRefineryBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        val inputTank: HTFluidTank = refinery.inputTank
        val outputTank: HTFluidTank = refinery.outputTank
        if (!player.isShiftKeyDown) {
            var interacted = false
            if (!level.isClientSide) {
                if (HTStackSlotHelper.interact(player, hand, stack, outputTank)) {
                    player.inventory.setChanged()
                    interacted = true
                } else if (HTStackSlotHelper.interact(player, hand, stack, inputTank)) {
                    player.inventory.setChanged()
                    interacted = true
                }
            }
            if (interacted) {
                return ItemInteractionResult.sidedSuccess(false)
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
    }
}
