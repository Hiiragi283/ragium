package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTMultiCapability
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTDrumBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTEntityBlock(type, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()

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
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        val handler: IFluidHandler = HTMultiCapability.FLUID.getCapability(stack) ?: return
        for (i: Int in handler.tankRange) {
            addFluidTooltip(handler.getFluidInTank(i), tooltipComponents::add, tooltipFlag, false)
        }
    }
}
