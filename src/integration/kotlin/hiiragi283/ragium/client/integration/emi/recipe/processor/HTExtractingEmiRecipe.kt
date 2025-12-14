package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputsEmiRecipe
import hiiragi283.ragium.common.recipe.HTBasicExtractingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class HTExtractingEmiRecipe : HTMultiOutputsEmiRecipe<HTBasicExtractingRecipe> {
    constructor(id: ResourceLocation, recipe: HTBasicExtractingRecipe) : super(
        RagiumEmiRecipeCategories.EXTRACTING,
        id,
        recipe,
    )

    constructor(holder: RecipeHolder<HTBasicExtractingRecipe>) : super(
        RagiumEmiRecipeCategories.EXTRACTING,
        holder,
    )

    override fun initInputs() {
        addInput(recipe.ingredient)
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(1))
    }
}
