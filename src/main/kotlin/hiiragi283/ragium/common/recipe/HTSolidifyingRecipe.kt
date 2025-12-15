package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTBasicFluidWithCatalystRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTSolidifyingRecipe(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult) :
    HTBasicFluidWithCatalystRecipe(ingredient, catalyst, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
