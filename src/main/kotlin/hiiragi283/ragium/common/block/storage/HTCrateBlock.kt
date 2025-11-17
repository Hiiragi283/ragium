package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTPlayerHandSlot
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class HTCrateBlock(tier: HTCrateTier, properties: Properties) : HTTypedEntityBlock<HTEntityBlockType>(tier.getBlockType(), properties) {
    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val crate: HTCrateBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.FAIL
        val slot: HTItemStackSlot = crate.slot
        val handSlot = HTPlayerHandSlot(player, hand)
        // プレイヤーがアイテムを持っている場合
        val stackInHand: ImmutableItemStack? = handSlot.getStack()
        if (stackInHand != null) {
            if (!level.isClientSide) {
                var remainder: ImmutableItemStack? = slot.insert(stackInHand, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
                if (remainder != stackInHand) {
                    remainder = slot.insert(stackInHand, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    handSlot.setStackUnchecked(remainder)
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
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

    override fun attack(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
        super.attack(state, level, pos, player)
        val crate: HTCrateBlockEntity = level.getTypedBlockEntity(pos) ?: return
        val slot: HTItemStackSlot = crate.slot
        val toExtract: Int = if (player.isShiftKeyDown) {
            slot.getStack()?.maxStackSize() ?: return
        } else {
            1
        }
        val extracted: ImmutableItemStack? = slot.extract(toExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        HTItemDropHelper.giveStackTo(player, extracted)
    }

    /*override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(RagiumCapabilities.FLUID.getCapabilityStacks(stack), tooltips::add, flag)
    }*/
}
