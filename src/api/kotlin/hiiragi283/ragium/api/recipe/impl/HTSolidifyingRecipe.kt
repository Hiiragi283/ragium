package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTSolidifyingRecipe(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult) :
    HTFluidWithCatalystToItemRecipe(ingredient, catalyst, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
