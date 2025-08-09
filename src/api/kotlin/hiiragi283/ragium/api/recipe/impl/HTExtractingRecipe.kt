package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTExtractingRecipe(ingredient: HTItemIngredient, result: HTItemResult) : HTItemToItemRecipe(ingredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
