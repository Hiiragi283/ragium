package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTChemicalWashingRecipe(
    ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    result: Ior<HTItemResult, HTFluidResult>,
    progressData: HTProgressData,
) : HTBasicItemOrFluidRecipe(ingredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CHEMICAL_WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CHEMICAL_WASHING.get()
}
