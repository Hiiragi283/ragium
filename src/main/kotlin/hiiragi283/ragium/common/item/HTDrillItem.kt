package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTEnergyItem
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

/**
 * @see [de.ellpeck.actuallyadditions.mod.items.DrillItem]
 */
class HTDrillItem(properties: Properties) :
    HTEnergyItem.User(
        properties
            .stacksTo(1)
            .component(DataComponents.UNBREAKABLE, Unbreakable(false))
            .component(
                DataComponents.TOOL,
                Tiers.NETHERITE.createToolProperties(RagiumModTags.Blocks.MINEABLE_WITH_DRILL),
            ),
    ) {
    companion object {
        @JvmField
        val ACTIONS: List<ItemAbility> = listOf(ItemAbilities.PICKAXE_DIG, ItemAbilities.SHOVEL_DIG)
    }

    override val energyUsage: Int = 160

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility in ACTIONS

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float = if (canConsumeEnergy(stack)) {
        if (state.`is`(RagiumModTags.Blocks.MINEABLE_WITH_DRILL)) {
            super.getDestroySpeed(stack, state)
        } else {
            1f
        }
    } else {
        0f
    }

    override fun mineBlock(
        stack: ItemStack,
        level: Level,
        state: BlockState,
        pos: BlockPos,
        miningEntity: LivingEntity,
    ): Boolean {
        if (level.isClientSide) return false
        if (state.getDestroySpeed(level, pos) == 0f) return false
        if (!canConsumeEnergy(stack)) return false
        val usage: Int = getEnergyUsage(stack)
        extractEnergy(stack, usage, false)
        return true
    }

    override fun isCorrectToolForDrops(stack: ItemStack, state: BlockState): Boolean =
        canConsumeEnergy(stack) && super.isCorrectToolForDrops(stack, state)
}
