package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

class HTEnchantingRecipe(ingredient: HTItemIngredient, val holder: Holder<Enchantment>) :
    HTBasicSingleItemRecipe(ingredient),
    HTRecipe.Fake<SingleRecipeInput> {
    fun getEnchantedBook(): ImmutableItemStack = EnchantmentInstance(holder, holder.value().maxLevel)
        .let(EnchantedBookItem::createForEnchantment)
        .toImmutableOrThrow()

    fun getRequiredExpFluid(): Int {
        val enchantment: Enchantment = holder.value()
        return HTExperienceHelper.fluidAmountFromExp(enchantment.getMaxCost(enchantment.maxLevel))
    }

    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when {
        test(input) -> getEnchantedBook()
        else -> null
    }

    override fun isIncompleteResult(): Boolean = !holder.isBound
}
