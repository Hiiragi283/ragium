package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.input.HTLocationRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.neoforged.neoforge.fluids.FluidStack

class RTResourceExtractingRecipe(
    val condition: HTBiomeCondition,
    val result: HTFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.Located,
    HTRecipeFactories.Located<FluidStack>,
    HTProgressRecipe.Simple<HTLocationRecipeInput>,
    HTSerializableRecipe<HTLocationRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTResourceExtractingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTBiomeCondition.CODEC.fieldOf("condition").forGetter(RTResourceExtractingRecipe::condition),
                HTFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTResourceExtractingRecipe::result),
                HTProgressData.CODEC.forGetter(RTResourceExtractingRecipe::progressData)
            ).apply(instance, ::RTResourceExtractingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTResourceExtractingRecipe> = StreamCodec.composite(
            HTBiomeCondition.STREAM_CODEC,
            RTResourceExtractingRecipe::condition,
            HTFluidResult.STREAM_CODEC,
            RTResourceExtractingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTResourceExtractingRecipe::progressData,
            ::RTResourceExtractingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTResourceExtractingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: BlockInWorld): Boolean = condition.test(input.level.getBiome(input.pos))

    override fun apply(input: BlockInWorld): FluidStack = result.create()

    override fun getSerializer(): RecipeSerializer<RTResourceExtractingRecipe> =
        RagiumRecipeSerializers.RESOURCE_EXTRACTING

    override fun getType(): HTRecipeType<RTResourceExtractingRecipe> = RagiumRecipeTypes.RESOURCE_EXTRACTING
}
