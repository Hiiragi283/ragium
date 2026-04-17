package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.HTBasicEnchantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.Enchantment

class HTHolderEnchantingRecipe(ingredient: HTItemIngredient, val holder: Holder<Enchantment>) :
    HTBasicEnchantingRecipe.Serializable(ingredient) {
    override val requiredExpAmount: Int get() {
        val enchantment: Enchantment = holder.value()
        return enchantment.getMaxCost(enchantment.maxLevel)
    }

    override fun applyEnchantment(stack: ItemStack): ItemStack {
        val result: ItemStack = stack.copyWithCount(1)
        result.enchant(holder, holder.value().maxLevel)
        return result
    }

    override fun testBase(stack: ItemStack): Boolean = stack.supportsEnchantment(holder)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.HOLDER_ENCHANTING
}
