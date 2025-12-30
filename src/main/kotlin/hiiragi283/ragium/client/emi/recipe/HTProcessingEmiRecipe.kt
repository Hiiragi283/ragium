package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTProcessingEmiRecipe<RECIPE : HTProcessingRecipe> : HTEmiHolderRecipe<RECIPE> {
    private val backgroundTex: String

    constructor(
        backgroundTex: String,
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : super(category, id, recipe) {
        this.backgroundTex = backgroundTex
    }

    constructor(
        backgroundTex: String,
        category: HTEmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
    ) : super(category, holder, category.bounds) {
        this.backgroundTex = backgroundTex
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(
            RagiumAPI.id("textures/gui/container/$backgroundTex.png"),
            0,
            0,
            displayWidth,
            displayHeight,
            25,
            17,
        )
        widgets.addArrow(getArrowX(), getPosition(1), recipe.time)
    }

    protected open fun getArrowX(): Int = getPosition(3.5)
}
