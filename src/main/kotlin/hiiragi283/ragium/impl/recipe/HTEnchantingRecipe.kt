package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.single.HTExpRequiredRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

class HTEnchantingRecipe(val ingredient: HTItemIngredient, val holder: Holder<Enchantment>) : HTExpRequiredRecipe {
    fun getEnchantedBook(): ImmutableItemStack = EnchantmentInstance(holder, holder.value().maxLevel)
        .let(EnchantedBookItem::createForEnchantment)
        .toImmutableOrThrow()

    override fun getRequiredExpFluid(): Int {
        val enchantment: Enchantment = holder.value()
        return HTExperienceHelper.fluidAmountFromExp(enchantment.getMaxCost(enchantment.maxLevel))
    }

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when {
        test(input) -> getEnchantedBook()
        else -> null
    }

    override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || !holder.isBound

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING

    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
