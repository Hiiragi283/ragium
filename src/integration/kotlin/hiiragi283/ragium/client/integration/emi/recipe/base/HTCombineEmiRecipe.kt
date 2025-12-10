package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addPlus
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTCombineEmiRecipe<RECIPE : Recipe<*>> : HTEmiHolderRecipe<RECIPE> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: RECIPE) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) : super(category, holder)

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.addPlus(getPosition(1), getPosition(0))

        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addSlot(input(1), getPosition(2), getPosition(0))

        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)
        widgets.addSlot(input(2), getPosition(1), getPosition(2))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
