package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class HTDrumBlock<BE : HTDrumBlockEntity>(type: HTDeferredBlockEntityType<BE>, properties: Properties) :
    HTEntityBlock<BE>(type, properties) {
    companion object {
        @JvmStatic
        fun create(type: HTDeferredBlockEntityType<out HTDrumBlockEntity>): (Properties) -> HTDrumBlock<out HTDrumBlockEntity> =
            { prop: Properties -> HTDrumBlock(type, prop) }
    }

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
        val handler: IFluidHandlerItem = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return
        for (i: Int in (0 until handler.tanks)) {
            addFluidTooltip(handler.getFluidInTank(i), tooltipComponents::add, tooltipFlag)
        }
    }
}
