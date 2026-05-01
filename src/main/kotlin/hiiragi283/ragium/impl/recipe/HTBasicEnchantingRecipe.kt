package hiiragi283.ragium.impl.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import net.minecraft.world.item.ItemStack

abstract class HTBasicEnchantingRecipe(val ingredient: HTItemIngredient) : HTEnchantingRecipe {
    abstract val requiredExpAmount: Int

    abstract fun applyEnchantment(stack: ItemStack): ItemStack

    protected abstract fun testBase(stack: ItemStack): Boolean

    override fun test(base: ItemStack, addition: ItemStack, expAmount: Int): Boolean =
        testBase(base) && ingredient.test(addition) && expAmount >= getRequiredExpAmount(base, addition)

    override fun getRequiredExpAmount(base: ItemStack, addition: ItemStack): Int = requiredExpAmount

    override fun getRequiredAdditionAmount(base: ItemStack, addition: ItemStack, expAmount: Int): Int =
        ingredient.getRequiredAmount(addition)

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack, thirdInput: Int): ItemStack = applyEnchantment(firstInput)
}
