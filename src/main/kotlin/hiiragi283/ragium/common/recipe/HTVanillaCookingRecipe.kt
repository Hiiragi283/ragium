package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe

open class HTVanillaCookingRecipe :
    HTVanillaSingleItemRecipe<AbstractCookingRecipe>,
    HTRecipe.Modifiable<HTVanillaCookingRecipe> {
    constructor(
        recipe: AbstractCookingRecipe,
        ingredient: HTItemIngredient,
        resultFactory: HTVanillaResultFactory,
    ) : super(recipe, ingredient, resultFactory)

    constructor(recipe: AbstractCookingRecipe) : super(recipe)

    val cookingTime: Int = recipe.cookingTime
    val experience: Float = recipe.experience

    override fun copyAndMultiply(multiplier: Int): HTVanillaCookingRecipe = HTVanillaCookingRecipe(
        this.recipe,
        this.ingredient.copyWithCount { it * multiplier },
        this.resultFactory::assemble.andThen { stack: ItemStack -> stack.copyWithCount(stack.count * multiplier) },
    )
}
