package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class RTElectrolyzingRecipe(
    val ingredient: HTFluidIngredient,
    val result: Result,
    override val progressData: HTProgressData
) : HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<RTElectrolyzingRecipe.Output>,
    HTProgressRecipe.Simple<HTRecipeInput>,
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTElectrolyzingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTElectrolyzingRecipe::ingredient),
                Result.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTElectrolyzingRecipe::result),
                HTProgressData.CODEC.forGetter(RTElectrolyzingRecipe::progressData)
            ).apply(instance, ::RTElectrolyzingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTElectrolyzingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            RTElectrolyzingRecipe::ingredient,
            Result.STREAM_CODEC,
            RTElectrolyzingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTElectrolyzingRecipe::progressData,
            ::RTElectrolyzingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTElectrolyzingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: FluidInstance): Boolean = ingredient.test(input)

    override fun apply(input: FluidInstance): Output = result.create()

    override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)

    override fun getSerializer(): RecipeSerializer<RTElectrolyzingRecipe> = RagiumRecipeSerializers.ELECTROLYZING

    override fun getType(): RecipeType<RTElectrolyzingRecipe> = RagiumRecipeTypes.ELECTROLYZING

    //    Output    //

    @JvmRecord
    data class Output(val left: FluidStack, val right: FluidStack, val item: ItemStack)

    //    Result    //

    @JvmRecord
    data class Result(val left: HTFluidResult, val right: HTFluidResult, val item: Optional<HTItemResult>) {
        companion object {
            @JvmField
            val CODEC: Codec<Result> = HTCodecs.record { instance ->
                instance.group(
                    HTFluidResult.CODEC.fieldOf("left").forGetter(Result::left),
                    HTFluidResult.CODEC.fieldOf("right").forGetter(Result::right),
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM).forGetter(Result::item)
                ).apply(instance, ::Result)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = StreamCodec.composite(
                HTFluidResult.STREAM_CODEC,
                Result::left,
                HTFluidResult.STREAM_CODEC,
                Result::right,
                ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
                Result::item,
                ::Result
            )
        }

        fun create(): Output = Output(right.create(), left.create(), item.createOrEmpty())
    }
}
