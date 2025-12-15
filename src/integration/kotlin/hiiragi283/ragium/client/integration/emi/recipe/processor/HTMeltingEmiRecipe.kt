package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.common.recipe.HTBasicMeltingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMeltingEmiRecipe(holder: RecipeHolder<HTBasicMeltingRecipe>) :
    HTEmiHolderRecipe<HTBasicMeltingRecipe>(RagiumEmiRecipeCategories.MELTING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        val input: EmiIngredient = input(0)
        widgets.addSlot(input, getPosition(1), getPosition(0))

        val remainders: EmiIngredient = input.emiStacks
            .map(EmiStack::getRemainder)
            .filterNot(EmiStack::isEmpty)
            .let(EmiIngredient::of)
        widgets.addSlot(remainders, getPosition(1), getPosition(2))
        // Output
        widgets.addTank(output(0), getPosition(4.5), getPosition(0)).recipeContext(this)
    }
}
