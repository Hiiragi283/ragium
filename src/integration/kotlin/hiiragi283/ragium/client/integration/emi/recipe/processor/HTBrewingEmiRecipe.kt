package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.base.HTCombineEmiRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.RecipeHolder

class HTBrewingEmiRecipe(holder: RecipeHolder<HTBrewingRecipe>) :
    HTCombineEmiRecipe<HTBrewingRecipe>(RagiumEmiRecipeCategories.BREWING, holder) {
    override fun getFluidIngredient(recipe: HTBrewingRecipe): EmiIngredient = HTBrewingRecipe.FLUID_INGREDIENT.toEmi()

    override fun getResult(recipe: HTBrewingRecipe): EmiStack =
        HTPotionHelper.createPotion(RagiumItems.POTION_DROP, recipe.contents).toEmi()
}
