package hiiragi283.ragium.common.block.generator

import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.entity.generator.HTBoilerBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidUtil

class HTBoilerBlock(properties: Properties) : HTMachineBlock(RagiumBlockEntityTypes.BOILER, properties) {
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
            val boilerEntity: HTBoilerBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.FAIL
            val handler = HTFluidHandler { listOf(boilerEntity.waterTank) }
            val moved: Boolean = FluidUtil.interactWithFluidHandler(player, hand, handler)
            if (moved) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }
        return result
    }
}
