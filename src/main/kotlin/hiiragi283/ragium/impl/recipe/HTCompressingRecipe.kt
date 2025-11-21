package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCompressingRecipe(ingredient: HTItemIngredient, result: HTItemResult) : HTBasicSingleItemRecipe(ingredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING.get()
}
