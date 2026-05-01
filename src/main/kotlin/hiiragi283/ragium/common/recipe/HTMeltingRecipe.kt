package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.impl.recipe.HTBasicItemToFluidRecipe
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTMeltingRecipe(ingredient: HTItemIngredient, result: HTFluidResult, progressData: HTProgressData) :
    HTBasicItemToFluidRecipe(ingredient, result, progressData),
    HTProgressRecipe.Simple<SingleRecipeInput>,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMeltingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTMeltingRecipe::result),
                    HTProgressData.CODEC.forGetter(HTMeltingRecipe::progressData),
                ).apply(instance, ::HTMeltingRecipe)
        }
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
