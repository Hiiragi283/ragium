package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputsEmiRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTItemWithCatalystEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicItemWithCatalystRecipe>) :
    HTMultiOutputsEmiRecipe<HTBasicItemWithCatalystRecipe>(category, holder) {
    init {
        when (recipe) {
            is HTCompressingRecipe -> {
                addInput(recipe.required)
                addCatalyst(recipe.optional.getOrNull())
            }

            is HTExtractingRecipe -> {
                addInput(recipe.required)
                addCatalyst(recipe.optional.getOrNull())
            }

            is HTSimulatingRecipe -> {
                addInput(recipe.optional.getOrNull())
                addCatalyst(recipe.required)
            }
        }
        addOutputs(recipe.results)
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(catalyst(0), getPosition(1), getPosition(2)).catalyst(true)
    }
}
