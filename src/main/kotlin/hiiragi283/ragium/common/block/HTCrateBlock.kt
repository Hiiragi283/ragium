package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTTypedEntityBlock
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.tier.HTCrateTier
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult

class HTCrateBlock(tier: HTCrateTier, properties: Properties) : HTTypedEntityBlock<HTEntityBlockType>(tier.getBlockType(), properties) {
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

    /*override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(RagiumCapabilities.FLUID.getCapabilityStacks(stack), tooltips::add, flag)
    }*/
}
