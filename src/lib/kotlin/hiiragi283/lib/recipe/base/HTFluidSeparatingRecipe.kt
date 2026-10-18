package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTFluidSeparatingRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

open class HTFluidSeparatingRecipe(
    val ingredient: HTFluidIngredient,
    val result: Result,
    override val progressData: HTProgressData
) : HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<HTFluidSeparatingRecipe.Output>,
    HTProgressRecipe.Simple<HTSingleFluidRecipeInput> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTFluidSeparatingRecipe> codec(
            factory: HTFluidSeparatingRecipeBuilder.Factory<RECIPE>
        ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTFluidSeparatingRecipe::ingredient),
                Result.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTFluidSeparatingRecipe::result),
                HTProgressData.CODEC.forGetter(HTFluidSeparatingRecipe::progressData)
            ).apply(instance, factory::create)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTFluidSeparatingRecipe> = codec(::HTFluidSeparatingRecipe)

        @JvmStatic
        fun <RECIPE : HTFluidSeparatingRecipe> streamCodec(
            factory: HTFluidSeparatingRecipeBuilder.Factory<RECIPE>
        ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            HTFluidSeparatingRecipe::ingredient,
            Result.STREAM_CODEC,
            HTFluidSeparatingRecipe::result,
            HTProgressData.STREAM_CODEC,
            HTFluidSeparatingRecipe::progressData,
            factory::create
        )
    }

    override fun test(input: FluidInstance): Boolean = ingredient.test(input)

    override fun apply(input: FluidInstance): Output = result.create()

    override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)

    //    Output    //

    @JvmRecord
    data class Output(val left: FluidStack, val right: FluidStack, val item: ItemStack)

    //    Result    //

    @JvmRecord
    data class Result(val fluids: Nel<HTFluidResult>, val item: Optional<HTItemResult>) {
        companion object {
            @JvmField
            val CODEC: Codec<Result> = HTCodecs.record { instance ->
                instance.group(
                    HTFluidResult.CODEC
                        .compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS, 2)
                        .forGetter(Result::fluids),
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM).forGetter(Result::item)
                ).apply(instance, ::Result)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = StreamCodec.composite(
                HTFluidResult.STREAM_CODEC.nelOf(),
                Result::fluids,
                ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
                Result::item,
                ::Result
            )
        }

        fun create(): Output = Output(fluids.head.create(), fluids.getOrNull(1).createOrEmpty(), item.createOrEmpty())
    }
}
