package hiiragi283.ragium.common.block.entity.bus

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.HTBasicEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

open class HTFluidBusBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTBasicEntityBlock(type, properties) {
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
}
