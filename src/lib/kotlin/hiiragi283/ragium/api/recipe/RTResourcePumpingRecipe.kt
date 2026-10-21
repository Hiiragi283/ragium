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

class RTResourcePumpingRecipe(condition: HTBiomeCondition, result: HTFluidResult, progressData: HTProgressData) :
    HTLocationToRecipe.BasicFluid(condition, result, progressData),
    HTSerializableRecipe<HTLocationRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTResourcePumpingRecipe> = codec(::RTResourcePumpingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTResourcePumpingRecipe> =
            streamCodec(::RTResourcePumpingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTResourcePumpingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun getSerializer(): RecipeSerializer<RTResourcePumpingRecipe> = RagiumRecipeSerializers.RESOURCE_PUMPING

    override fun getType(): RecipeType<RTResourcePumpingRecipe> = RagiumRecipeTypes.RESOURCE_PUMPING
}
