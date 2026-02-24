package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.common.recipe.base.HTBasicItemOrFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMeltingRecipe(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, time: Int) :
    HTBasicItemOrFluidRecipe(ingredient, result, time) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
