package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.support.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTCompressingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTBasicItemToItemRecipe(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCompressingRecipe> = codec(::HTCompressingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING
}
