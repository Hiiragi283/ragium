package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTFluidSeparatingRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.serialization.network.optionalOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
interface HTFluidSeparatingRecipe : HTFluidToRecipe<HTFluidSeparatingRecipe.Output> {
    //    Output    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    @JvmRecord
    data class Output(val left: FluidStack, val right: FluidStack, val item: ItemStack)

    //    Result    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    @JvmRecord
    data class Result(val fluids: Nel<HTFluidResult>, val item: Optional<HTItemResult>) {
        companion object {
            @JvmField
            val CODEC: Codec<Result> = HTCodecs.record { instance ->
                instance.group(
                    HTFluidResult.CODEC
                        .compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS, 2)
                        .forGetter(Result::fluids),
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM).forGetter(Result::item),
                ).apply(instance, ::Result)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = StreamCodec.composite(
                HTFluidResult.STREAM_CODEC.nelOf(),
                Result::fluids,
                HTItemResult.STREAM_CODEC.optionalOf(),
                Result::item,
                ::Result,
            )
        }

        fun create(): Output = Output(fluids.head.create(), fluids.getOrNull(1).createOrEmpty(), item.createOrEmpty())
    }

    //    Basic    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic(override val ingredient: HTFluidIngredient, val result: Result, override val progressData: HTProgressData) :
        HTFluidSeparatingRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTFluidSeparatingRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    Result.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressRecipe.Holder.progressCodec(),
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTFluidSeparatingRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                StreamCodec.composite(
                    HTFluidIngredient.STREAM_CODEC,
                    Basic::ingredient,
                    Result.STREAM_CODEC,
                    Basic::result,
                    HTProgressData.STREAM_CODEC,
                    Basic::progressData,
                    factory::create,
                )
        }

        override fun getProgressData(input: FluidInstance): HTProgressData = progressData

        override fun apply(input: FluidInstance): Output = result.create()

        override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)
    }
}
