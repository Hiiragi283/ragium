package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTVanillaCookingRecipe : HTVanillaSingleItemRecipe<AbstractCookingRecipe> {
    constructor(
        recipe: AbstractCookingRecipe,
        ingredient: HTItemIngredient,
        resultFactory: HTVanillaResultFactory<SingleRecipeInput>,
    ) : super(recipe, ingredient, resultFactory)

    constructor(recipe: AbstractCookingRecipe) : super(recipe)

    val cookingTime: Int = recipe.cookingTime
    val experience: Float = recipe.experience
}
