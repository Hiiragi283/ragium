package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTCatalysedFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTFreezingRecipe(ingredient: HTFluidIngredient, catalyst: HTItemCatalyst, result: HTItemResult, progressData: HTProgressData) :
    HTCatalysedFluidToRecipe.BasicItem(ingredient, catalyst, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTFreezingRecipe> = codec(HTFluidIngredient.CODEC, HTItemResult.CODEC, ::RTFreezingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTFreezingRecipe> =
            streamCodec(HTFluidIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, ::RTFreezingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTFreezingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun getSerializer(): RecipeSerializer<RTFreezingRecipe> = RagiumRecipeSerializers.FREEZING

    override fun getType(): RecipeType<RTFreezingRecipe> = RagiumRecipeTypes.FREEZING
}
