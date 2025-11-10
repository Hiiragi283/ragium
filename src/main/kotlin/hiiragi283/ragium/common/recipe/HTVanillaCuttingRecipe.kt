package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.impl.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.mixin.SingleItemRecipeAccessor
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.StonecutterRecipe

class HTVanillaCuttingRecipe(recipe: StonecutterRecipe) :
    HTItemToChancedItemRecipeBase(
        HTItemIngredient.of(recipe.ingredients[0]),
        (recipe as SingleItemRecipeAccessor)
            .result
            .let(HTResultHelper::item)
            .let(::HTChancedItemResult)
            .let(::listOf),
    ) {
    @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
    override fun getSerializer(): RecipeSerializer<*> = throw UnsupportedOperationException()

    @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
    override fun getType(): RecipeType<*> = throw UnsupportedOperationException()
}
