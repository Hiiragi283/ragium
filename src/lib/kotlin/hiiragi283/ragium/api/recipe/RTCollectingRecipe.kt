package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTLocationToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.input.HTLocationRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTCollectingRecipe(condition: HTBiomeCondition, result: HTFluidResult, progressData: HTProgressData) :
    HTLocationToRecipe.BasicFluid(condition, result, progressData),
    HTSerializableRecipe<HTLocationRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTCollectingRecipe> = codec(::RTCollectingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTCollectingRecipe> =
            streamCodec(::RTCollectingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTCollectingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun getSerializer(): RecipeSerializer<RTCollectingRecipe> = RagiumRecipeSerializers.COLLECTING

    override fun getType(): RecipeType<RTCollectingRecipe> = RagiumRecipeTypes.COLLECTING
}
