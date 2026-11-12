package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTFluidSeparatingRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.IorNel
import hiiragi283.lib.util.toPair
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

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
    data class Output(val fluids: List<FluidStack>, val item: ItemStack) {
        constructor(fluid: FluidStack, item: ItemStack) : this(listOf(fluid), item)

        constructor(pair: Pair<List<FluidStack>, ItemStack>) : this(pair.first, pair.second)
    }

    //    Result    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    @JvmRecord
    data class Result(val content: IorNel<HTFluidResult, HTItemResult>) : HTRecipeResult<Output> {
        companion object {
            @JvmField
            val CODEC: MapCodec<Result> = HTCodecs.ior(
                HTFluidResult.CODEC.compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS, 2),
                HTItemResult.CODEC.fieldOf(HTConstants.ITEM),
            ).xmap(::Result, Result::content)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = HTStreamCodecs.ior(
                HTFluidResult.STREAM_CODEC.nelOf(),
                HTItemResult.STREAM_CODEC,
            ).map(::Result, Result::content)
        }

        override fun getId(): Identifier? = content.fold(
            { it.head.getId() },
            HTItemResult::getId,
            { fluids: Nel<HTFluidResult>, item: HTItemResult -> fluids.head.getId() ?: item.getId() },
        )

        override fun create(): Output = content
            .mapLeft { it.map(HTFluidResult::create) }
            .mapRight { it.create() }
            .toPair(::listOf, ItemStack::EMPTY)
            .let(::Output)
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
