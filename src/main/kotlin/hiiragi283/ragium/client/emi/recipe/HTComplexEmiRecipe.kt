package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.world.item.crafting.RecipeHolder

class HTComplexEmiRecipe<RECIPE : HTComplexRecipe>(
    private val config: HTMachineConfig,
    backgroundTex: String,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(backgroundTex, category, holder) {
    companion object {
        @JvmStatic
        fun drying(holder: RecipeHolder<HTDryingRecipe>): HTComplexEmiRecipe<HTDryingRecipe> = HTComplexEmiRecipe(
            RagiumConfig.COMMON.processor.dryer,
            RagiumConst.DRYER,
            RagiumEmiRecipeCategories.DRYING,
            holder,
        )

        @JvmStatic
        fun mixing(holder: RecipeHolder<HTMixingRecipe>): HTComplexEmiRecipe<HTMixingRecipe> = HTComplexEmiRecipe(
            RagiumConfig.COMMON.processor.mixer,
            RagiumConst.MIXER,
            RagiumEmiRecipeCategories.MIXING,
            holder,
        )
    }

    init {
        addInput(recipe.getItemIngredient())
        addInput(recipe.getFluidIngredient())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(2), getPosition(1.5), recipe.time)

        // Input
        widgets.addInput(0, getPosition(2), getPosition(0.5))
        widgets.addTank(input(1), getPosition(0.5), getCapacity(RagiumFluidConfigType.FIRST_INPUT))

        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(1), true)
        widgets.addTank(output(1), getPosition(7), getCapacity(RagiumFluidConfigType.FIRST_OUTPUT)).recipeContext(this)
    }

    override fun getConfig(): HTMachineConfig = config
}
