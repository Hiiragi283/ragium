package hiiragi283.ragium.support.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import net.minecraft.world.item.ItemStack

abstract class HTBasicEnchantingRecipe(val ingredient: HTItemIngredient) : HTEnchantingRecipe {
    abstract val requiredExpAmount: Int

    abstract fun applyEnchantment(stack: ItemStack): ItemStack

    protected abstract fun testBase(stack: ItemStack): Boolean

    override fun test(first: ItemStack, second: ItemStack, third: Int): Boolean = testBase(first) && ingredient.test(second) && third >= getRequiredExpAmount(first, second)

    override fun getRequiredExpAmount(base: ItemStack, addition: ItemStack): Int = requiredExpAmount

    override fun getRequiredAdditionAmount(base: ItemStack, addition: ItemStack, expAmount: Int): Int = ingredient.getMatchingStack(addition).count

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack, thirdInput: Int): ItemStack = applyEnchantment(firstInput)
}
