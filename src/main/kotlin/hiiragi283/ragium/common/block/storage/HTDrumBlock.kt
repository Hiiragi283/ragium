package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.extension.getTypedBlockEntity
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.text.addFluidTooltip
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class HTDrumBlock(tier: HTDrumTier, properties: Properties) : HTTypedEntityBlock<HTEntityBlockType>(tier.getBlockType(), properties) {
    /**
     * @see mekanism.common.block.basic.BlockFluidTank.useItemOn
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
        if (stack.isEmpty) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        val tank: HTFluidTank = level
            .getTypedBlockEntity<HTDrumBlockEntity>(pos)
            ?.tank
            ?: return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        when {
            level.isClientSide -> return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            !player.isShiftKeyDown -> {
                if (HTStackSlotHelper.interact(player, hand, stack, tank)) {
                    player.inventory.setChanged()
                }
                return ItemInteractionResult.SUCCESS
            }
            else -> return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }
    }

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player,
    ): ItemStack {
        val stack: ItemStack = super.getCloneItemStack(state, target, level, pos, player)
        level.getBlockEntity(pos)?.collectComponents()?.let(stack::applyComponents)
        return stack
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(HTFluidCapabilities.getCapabilityStacks(stack), tooltips::add, flag)
    }
}
