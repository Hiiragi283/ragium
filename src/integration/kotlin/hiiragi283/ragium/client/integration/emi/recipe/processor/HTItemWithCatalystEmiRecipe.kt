package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputsEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTItemWithCatalystEmiRecipe : HTMultiOutputsEmiRecipe<HTBasicItemWithCatalystRecipe> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTBasicItemWithCatalystRecipe) : super(
        category,
        id,
        recipe,
    )

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicItemWithCatalystRecipe>) : super(
        category,
        holder,
    )

    companion object {
        @JvmStatic
        fun compressing(holder: RecipeHolder<HTBasicItemWithCatalystRecipe>): HTItemWithCatalystEmiRecipe =
            HTItemWithCatalystEmiRecipe(RagiumEmiRecipeCategories.COMPRESSING, holder)

        @JvmStatic
        fun extracting(id: ResourceLocation, recipe: HTBasicItemWithCatalystRecipe): HTItemWithCatalystEmiRecipe =
            HTItemWithCatalystEmiRecipe(RagiumEmiRecipeCategories.EXTRACTING, id, recipe)

        @JvmStatic
        fun extracting(holder: RecipeHolder<HTBasicItemWithCatalystRecipe>): HTItemWithCatalystEmiRecipe =
            HTItemWithCatalystEmiRecipe(RagiumEmiRecipeCategories.EXTRACTING, holder)
    }

    override fun initInputs() {
        addInput(recipe.required)
        addCatalyst(recipe.optional.getOrNull())
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addCatalyst(0, getPosition(1), getPosition(2))
    }
}
