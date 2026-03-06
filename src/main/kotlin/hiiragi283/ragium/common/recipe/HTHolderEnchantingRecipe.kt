package hiiragi283.ragium.common.recipe

import com.google.common.primitives.Ints
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

class HTHolderEnchantingRecipe(val ingredient: HTItemIngredient, val holder: Holder<Enchantment>) : HTEnchantingRecipe {
    val requiredExpAmount: Int get() {
        val enchantment: Enchantment = holder.value()
        return enchantment
            .getMaxCost(enchantment.maxLevel)
            .let(HTExperienceHelper::fluidAmountFromExp)
            .let(Ints::saturatedCast)
    }

    override fun testBook(stack: ItemStack): Boolean = stack.`is`(Items.BOOK)

    override fun testItem(stack: ItemStack): Boolean = ingredient.test(stack)

    override fun getRequiredExpAmount(input: HTEnchantingRecipe.Input): Int = requiredExpAmount

    override fun getRequiredItemAmount(input: HTEnchantingRecipe.Input): Int = ingredient.amount

    override fun assemble(input: HTEnchantingRecipe.Input, registries: HolderLookup.Provider): ItemStack {
        var stack: ItemStack = input.book
        stack = stack.item.applyEnchantments(stack, listOf(EnchantmentInstance(holder, holder.value().maxLevel)))
        return stack
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.HOLDER_ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()
}
