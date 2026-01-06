package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.world.item.crafting.RecipeHolder

class HTPyrolyzingEmiRecipe(holder: RecipeHolder<HTPyrolyzingRecipe>) :
    HTProcessingEmiRecipe<HTPyrolyzingRecipe>(RagiumConst.PYROLYZER, RagiumEmiRecipeCategories.PYROLYZING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.itemResult)
        addOutputs(recipe.fluidResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(1), getPosition(1.5), recipe.time)
        // Input
        widgets.addInput(0, getPosition(1), getPosition(0.5))
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0.5))
        widgets.addTank(output(1), getPosition(7), getCapacity(RagiumFluidConfigType.FIRST_OUTPUT)).recipeContext(this)
    }

    override fun getArrowX(): Int = getPosition(2.5)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.pyrolyzer
}
