package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCuttingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTItemToItemRecipe>) :
    HTMultiOutputEmiRecipe<HTItemToItemRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun getCatalystStack(): EmiIngredient = output(0).copy().setAmount(1)

    override fun shouldCatalyst(): Boolean = true
}
