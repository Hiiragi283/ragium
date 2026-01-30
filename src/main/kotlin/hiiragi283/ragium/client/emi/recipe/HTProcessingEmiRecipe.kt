package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTProcessingEmiRecipe<RECIPE : HTViewProcessingRecipe> : HTEmiHolderRecipe<RECIPE> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: RECIPE) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) : super(category, holder)

    //    Extensions    //

    fun WidgetHolder.add2x2Slots(x: Int = getPosition(5.5), y: Int = getPosition(0.5), ingredient: (Int) -> EmiIngredient = ::output) {
        this.addSlot(ingredient(0), x, y, HTBackgroundType.OUTPUT)
        this.addSlot(ingredient(1), x + getPosition(1), y, HTBackgroundType.OUTPUT)
        this.addSlot(ingredient(2), x, y + getPosition(1), HTBackgroundType.OUTPUT)
        this.addSlot(ingredient(3), x + getPosition(1), y + getPosition(1), HTBackgroundType.OUTPUT)
    }
}
