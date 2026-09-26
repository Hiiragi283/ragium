package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTFluidToDoubleFluidRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類の液体から2種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
interface HTFluidToDoubleFluidRecipe :
    HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<Pair<FluidStack, FluidStack>>,
    HTProgressRecipe<HTSingleFluidRecipeInput> {

    fun asSingleOutput(): HTFluidToFluidRecipe = object : HTFluidToFluidRecipe {
        override fun test(input: FluidInstance): Boolean = this@HTFluidToDoubleFluidRecipe.test(input)

        override fun getMatchingStack(input: FluidInstance): FluidInstance =
            this@HTFluidToDoubleFluidRecipe.getMatchingStack(input)

        override fun apply(input: FluidInstance): FluidStack = this@HTFluidToDoubleFluidRecipe.apply(input).first

        override fun getProgressData(input: HTSingleFluidRecipeInput): HTProgressData =
            this@HTFluidToDoubleFluidRecipe.getProgressData(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    open class Basic(
        val ingredient: HTFluidIngredient,
        val result: Nel<HTFluidResult>,
        override val progressData: HTProgressData
    ) : HTFluidToDoubleFluidRecipe,
        HTProgressRecipe.Simple<HTSingleFluidRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTFluidToDoubleFluidRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                        HTFluidResult.CODEC
                            .compactNelFieldOf(HTConstants.RESULT, HTConstants.RESULTS, 2)
                            .forGetter(Basic::result),
                        HTProgressData.CODEC.forGetter(Basic::progressData)
                    ).apply(instance, factory::create)
                }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: HTFluidToDoubleFluidRecipeBuilder.Factory<RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTFluidIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTFluidResult.STREAM_CODEC.nelOf(),
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create
            )
        }

        override fun test(input: FluidInstance): Boolean = ingredient.test(input)

        override fun apply(input: FluidInstance): Pair<FluidStack, FluidStack> =
            result.head.create() to result.getOrNull(1).createOrEmpty()

        override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)
    }
}
