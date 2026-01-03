package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.world.item.crafting.RecipeHolder

class HTMeltingEmiRecipe(holder: RecipeHolder<HTMeltingRecipe>) :
    HTProcessingEmiRecipe<HTMeltingRecipe>(RagiumConst.MELTER, RagiumEmiRecipeCategories.MELTING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets.addInput(0, getPosition(2), getPosition(0.5))
        widgets.addBurning(getPosition(2), getPosition(1.5), recipe.time)
        // Output
        widgets
            .addTank(output(0), getPosition(5.5), getCapacity(RagiumFluidConfigType.FIRST_INPUT))
            .recipeContext(this)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.melter
}
