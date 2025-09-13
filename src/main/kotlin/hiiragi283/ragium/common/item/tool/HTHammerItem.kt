package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.extension.restDamage
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

open class HTHammerItem(tier: Tier, properties: Properties) : DiggerItem(tier, RagiumModTags.Blocks.MINEABLE_WITH_HAMMER, properties) {
    companion object {
        @JvmField
        val ABILITIES: Set<ItemAbility> = setOf(ItemAbilities.PICKAXE_DIG, ItemAbilities.SHOVEL_DIG)
    }

    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = stack.restDamage > 0

    override fun getCraftingRemainingItem(stack: ItemStack): ItemStack {
        if (hasCraftingRemainingItem(stack)) {
            val copied: ItemStack = stack.copy()
            copied.damageValue++
            return copied
        }
        return super.getCraftingRemainingItem(stack)
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility in ABILITIES
}
