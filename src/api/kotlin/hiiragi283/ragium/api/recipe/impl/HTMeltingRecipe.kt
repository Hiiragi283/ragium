package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMeltingRecipe(itemIngredient: HTItemIngredient, result: HTFluidResult) : HTItemToFluidRecipe(itemIngredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
