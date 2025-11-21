package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTSingleOutputEmiRecipe<RECIPE : HTBasicSingleOutputRecipe<*>> : HTEmiHolderRecipe<RECIPE> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: RECIPE) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) : super(category, holder)

    init {
        initInputs(recipe)

        addOutputs(recipe.result)
    }

    protected abstract fun initInputs(recipe: RECIPE)

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))

        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
