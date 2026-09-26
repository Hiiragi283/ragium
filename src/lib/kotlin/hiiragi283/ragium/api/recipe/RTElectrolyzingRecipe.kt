package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTElectrolyzingRecipe(
    val ingredient: HTFluidIngredient,
    val results: Nel<HTFluidResult>,
    override val progressData: HTProgressData
) : HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<RTElectrolyzingRecipe.ElectrolyzedResult>,
    HTProgressRecipe.Simple<HTSingleFluidRecipeInput>,
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTElectrolyzingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC
                    .fieldOf(HTConstants.INGREDIENT)
                    .forGetter(RTElectrolyzingRecipe::ingredient),
                HTFluidResult.CODEC
                    .compactNelFieldOf(HTConstants.RESULT, HTConstants.RESULTS, 3)
                    .forGetter(RTElectrolyzingRecipe::results),
                HTProgressData.CODEC.forGetter(RTElectrolyzingRecipe::progressData)
            ).apply(instance, ::RTElectrolyzingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTElectrolyzingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            RTElectrolyzingRecipe::ingredient,
            HTFluidResult.STREAM_CODEC.nelOf(),
            RTElectrolyzingRecipe::results,
            HTProgressData.STREAM_CODEC,
            RTElectrolyzingRecipe::progressData,
            ::RTElectrolyzingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTElectrolyzingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: FluidInstance): Boolean = ingredient.test(input)

    override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)

    override fun apply(input: FluidInstance): ElectrolyzedResult = ElectrolyzedResult(
        results.head.create(),
        results.getOrNull(1).createOrEmpty(),
        results.getOrNull(2).createOrEmpty()
    )

    override fun getSerializer(): RecipeSerializer<RTElectrolyzingRecipe> = RagiumRecipeSerializers.ELECTROLYZING

    override fun getType(): HTRecipeType<RTElectrolyzingRecipe> = RagiumRecipeTypes.ELECTROLYZING

    @JvmRecord
    data class ElectrolyzedResult(val right: FluidStack, val left: FluidStack, val main: FluidStack)
}
