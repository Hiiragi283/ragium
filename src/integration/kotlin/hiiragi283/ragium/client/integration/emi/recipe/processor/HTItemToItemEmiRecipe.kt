package hiiragi283.ragium.client.integration.emi.recipe.processor

import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTSingleOutputEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * @see mekanism.client.recipe_viewer.emi.recipe.ItemStackToItemStackEmiRecipe
 */
class HTItemToItemEmiRecipe : HTSingleOutputEmiRecipe<HTBasicSingleItemRecipe> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTBasicSingleItemRecipe) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicSingleItemRecipe>) : super(category, holder)

    override fun initInputs(recipe: HTBasicSingleItemRecipe) {
        addInput(recipe.ingredient)
    }
}
