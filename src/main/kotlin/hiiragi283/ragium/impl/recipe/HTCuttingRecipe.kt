package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTBasicItemToExtraItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTCuttingRecipe(ingredient: HTItemIngredient, result: HTItemResult, extra: Optional<HTItemResult>) :
    HTBasicItemToExtraItemRecipe(ingredient, result, extra) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CUTTING.get()
}
