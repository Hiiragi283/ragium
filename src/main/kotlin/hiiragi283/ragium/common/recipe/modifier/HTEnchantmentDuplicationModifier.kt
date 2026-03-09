package hiiragi283.ragium.common.recipe.modifier

import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.ragium.api.recipe.HTDuplicationModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper

data object HTEnchantmentDuplicationModifier : HTDuplicationModifier {
    override fun test(stack: ItemStack): Boolean = !EnchantmentHelper.getEnchantmentsForCrafting(stack).isEmpty

    override fun calculateExtraAmount(stack: ItemStack): Int = EnchantmentHelper
        .getEnchantmentsForCrafting(stack)
        .let(HTExperienceHelper::getTotalMaxCost)
        .let(HTExperienceHelper::fluidAmountFromExp)
}
