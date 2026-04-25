package hiiragi283.ragium.impl.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

abstract class HTBasicEnchantingRecipe(val ingredient: HTItemIngredient) : HTEnchantingRecipe {
    abstract val requiredExpAmount: Int

    abstract fun applyEnchantment(stack: ItemStack): ItemStack

    final override fun getRequiredExpAmount(input: HTEnchantingRecipe.Input): Int = requiredExpAmount

    final override fun getRequiredAdditionAmount(input: HTEnchantingRecipe.Input): Int = ingredient.getRequiredAmount(input.addition)

    final override fun test(input: HTEnchantingRecipe.Input): Boolean {
        val (base: ItemStack, addition: ItemStack, expAmount: Int) = input
        val bool1: Boolean = testBase(base)
        val bool2: Boolean = ingredient.test(addition)
        val bool3: Boolean = expAmount >= getRequiredExpAmount(input)
        return bool1 && bool2 && bool3
    }

    protected abstract fun testBase(stack: ItemStack): Boolean

    final override fun assemble(input: HTEnchantingRecipe.Input, preview: Boolean): ItemStack = applyEnchantment(input.base)

    abstract class Serializable(ingredient: HTItemIngredient) :
        HTBasicEnchantingRecipe(ingredient),
        HTEnchantingRecipe.Serializable {
        final override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()
    }
}
