package hiiragi283.ragium.client.integration.emi.recipe.processor

import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTSingleOutputEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidInputRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemWithFluidEmiRecipe : HTSingleOutputEmiRecipe<HTBasicItemWithFluidInputRecipe> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTBasicItemWithFluidInputRecipe) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicItemWithFluidInputRecipe>) : super(category, holder)

    override fun initInputs(recipe: HTBasicItemWithFluidInputRecipe) {
        addInput(recipe.itemIngredient)
        addInput(recipe.fluidIngredient)
    }
}
