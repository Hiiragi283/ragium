package hiiragi283.ragium.client.integration.emi.recipe.processor

import hiiragi283.ragium.api.item.createEnchantedBook
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.base.HTCombineEmiRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.crafting.RecipeHolder

class HTEnchantingEmiRecipe(holder: RecipeHolder<HTEnchantingRecipe>) :
    HTCombineEmiRecipe<HTEnchantingRecipe>(RagiumEmiRecipeCategories.ENCHANTING, holder) {
    init {
        val (left: HTItemIngredient, right: HTItemIngredient) = recipe.itemIngredients
        addInput(left)
        addInput(right)
        addInput(RagiumFluidContents.EXPERIENCE.toFluidEmi(recipe.getRequiredExpFluid()))
        addOutputs(recipe.holder.let(::createEnchantedBook).toEmi())
    }
}
