package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.ragium.impl.recipe.HTBasicEnchantingRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments

class HTCustomEnchantingRecipe(val supportedItems: HTItemIngredient, ingredient: HTItemIngredient, val enchantments: ItemEnchantments) :
    HTBasicEnchantingRecipe.Serializable(ingredient) {
    override val requiredExpAmount: Int = HTExperienceHelper.getTotalMaxCost(enchantments)

    override fun applyEnchantment(stack: ItemStack): ItemStack {
        val result: ItemStack = stack.copyWithCount(1)
        EnchantmentHelper.setEnchantments(result, enchantments)
        return result
    }

    override fun testBase(stack: ItemStack): Boolean = supportedItems.test(stack)

    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }
}
