package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.recipe.HTProcessingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTProcessingEmiRecipe<RECIPE: HTProcessingRecipe> : HTEmiHolderRecipe<RECIPE> {
    constructor(category: EmiRecipeCategory, holder: RecipeHolder<RECIPE>, bounds: HTBounds) : super(
        category,
        holder,
        bounds
    )

    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : super(category, id, recipe)

    constructor(
        category: HTEmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
    ) : super(category, holder, category.bounds)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getArrowX(), getPosition(1), recipe.time)
    }
    
    protected open fun getArrowX(): Int = getPosition(2.5)
}
