package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemAndFluidToItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTBathingRecipe(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTBasicItemAndFluidToItemRecipe(itemIngredient, fluidIngredient, true, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBathingRecipe> = codec(::HTBathingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BATHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BATHING
}
