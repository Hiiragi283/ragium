package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.restDamage
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTFuelDrillItem(tier: Tier, properties: Properties) :
    DiggerItem(
        tier,
        RagiumBlockTags.MINEABLE_WITH_DRILL,
        properties
            .stacksTo(1)
            .component(DataComponents.TOOL, tier.createToolProperties(RagiumBlockTags.MINEABLE_WITH_DRILL)),
    ) {
    companion object {
        @JvmField
        val ACTIONS: List<ItemAbility> = listOf(
            ItemAbilities.PICKAXE_DIG,
            ItemAbilities.SHOVEL_DIG,
            ItemAbilities.SHOVEL_FLATTEN,
        )
    }

    override fun canAttackBlock(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ): Boolean {
        if (!player.isCreative) {
            val stack: ItemStack = player.mainHandItem
            if (stack.restDamage <= 0) {
                return false
            }
        }
        return super.canAttackBlock(state, level, pos, player)
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = when (itemAbility) {
        in ACTIONS -> true
        else -> super.canPerformAction(stack, itemAbility)
    }

    //    Damage Handling    //

    private fun getFluid(stack: ItemStack): SimpleFluidContent =
        stack.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY)

    private fun getCapacity(stack: ItemStack): Int = stack.getCapability(Capabilities.FluidHandler.ITEM)?.getTankCapacity(0) ?: 0

    private fun getRemainAmount(stack: ItemStack): Int = getCapacity(stack) - getFluid(stack).amount

    override fun getDamage(stack: ItemStack): Int = Mth.clamp(getRemainAmount(stack), 0, getCapacity(stack))

    override fun getMaxDamage(stack: ItemStack): Int = getCapacity(stack)

    override fun setDamage(stack: ItemStack, damage: Int) {
        val diff: Int = stack.maxDamage - damage
        stack.getCapability(Capabilities.FluidHandler.ITEM)?.drain(diff, IFluidHandler.FluidAction.EXECUTE)
    }
}
