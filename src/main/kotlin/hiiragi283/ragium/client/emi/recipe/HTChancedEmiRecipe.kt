package hiiragi283.ragium.client.emi.recipe

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedEmiRecipe<RECIPE : HTChancedRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        addInputs()

        addOutputs(recipe.result)
        for ((result: HTItemResult, chance: Fraction) in recipe.extraResults) {
            addOutputs(result.toEmi().setChance(chance.toFloat()))
        }
    }

    protected abstract fun addInputs()
}
