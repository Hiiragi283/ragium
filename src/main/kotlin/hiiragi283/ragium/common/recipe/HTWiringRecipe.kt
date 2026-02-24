package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTWiringRecipe(ingredient: HTItemIngredient, result: HTItemResult, time: Int) :
    HTBasicItemToItemRecipe(ingredient, result, time) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WIRING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WIRING.get()
}
