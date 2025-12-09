package hiiragi283.ragium.client.integration.emi.recipe.device

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addPlus
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTRockGeneratingEmiRecipe(holder: RecipeHolder<HTRockGeneratingRecipe>) :
    HTEmiHolderRecipe<HTRockGeneratingRecipe>(RagiumEmiRecipeCategories.ROCK_GENERATING, holder) {
    init {
        addInput(recipe.left)
        recipe.right.map(::addInput, ::addInput)

        addCatalyst(recipe.bottom.getOrNull())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.addPlus(getPosition(1), getPosition(0))

        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0)).catalyst(true)
        widgets.addSlot(input(1), getPosition(2), getPosition(0)).catalyst(true)

        widgets.addCatalyst(0, getPosition(1), getPosition(2))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
