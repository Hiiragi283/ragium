package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addTank
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.toFluidEmi
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTChancedRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.crafting.RecipeHolder
import org.apache.commons.lang3.math.Fraction

class HTChancedEmiRecipe<RECIPE : HTChancedRecipe>(backgroundTex: String, category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(backgroundTex, category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HTCrushingRecipe>): HTChancedEmiRecipe<HTCrushingRecipe> =
            HTChancedEmiRecipe(RagiumConst.CRUSHER, RagiumEmiRecipeCategories.CRUSHING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        for ((result: HTItemResult, chance: Fraction) in recipe.extraResults) {
            addOutputs(result.toEmi().setChance(chance.toFloat()))
        }
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets.addTank(RagiumFluids.LUBRICANT.toFluidEmi(1000), getPosition(0.5), getPosition(0)).catalyst(true)
        widgets.addInput(0, getPosition(2), getPosition(0.5))

        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(0.5))
        widgets.addOutput(1, getPosition(6.5), getPosition(0.5))
        widgets.addOutput(2, getPosition(5.5), getPosition(1.5))
        widgets.addOutput(3, getPosition(6.5), getPosition(1.5))
    }
}
