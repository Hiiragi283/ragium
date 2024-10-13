package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.component.HTModularToolComponent
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.tag.BlockTags
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import team.reborn.energy.api.base.SimpleEnergyItem

class HTModularMiningToolItem(material: ToolMaterial, settings: Settings) :
    MiningToolItem(material, BlockTags.AIR, settings),
    SimpleEnergyItem {
    private fun getComponent(stack: ItemStack): HTModularToolComponent =
        stack.getOrDefault(HTModularToolComponent.COMPONENT_TYPE, HTModularToolComponent.DEFAULT)

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = super.use(world, user, hand)

    override fun useOnBlock(context: ItemUsageContext): ActionResult = context.stack
        .get(HTModularToolComponent.COMPONENT_TYPE)
        ?.useOnBlock(context)
        ?: super.useOnBlock(context)

    override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean = false

    override fun isCorrectForDrops(stack: ItemStack, state: BlockState): Boolean = getComponent(stack).isSuitableFor(state)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        getComponent(stack).appendTooltip(context, tooltip::add, type)
    }

    //    SimpleEnergyItem    //

    override fun getEnergyCapacity(stack: ItemStack): Long = getComponent(stack).tier.energyCapacity

    override fun getEnergyMaxInput(stack: ItemStack): Long = getEnergyCapacity(stack)

    override fun getEnergyMaxOutput(stack: ItemStack): Long = 0
}
