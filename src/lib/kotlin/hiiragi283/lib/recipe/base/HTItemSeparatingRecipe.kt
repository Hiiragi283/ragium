package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTItemSeparatingRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.serialization.network.optionalOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

interface HTItemSeparatingRecipe : HTItemToRecipe<HTItemSeparatingRecipe.Output> {
    //    Output    //

    @JvmRecord
    data class Output(val items: List<ItemStack>, val fluid: FluidStack) {
        constructor(item: ItemStack, fluid: FluidStack) : this(listOf(item), fluid)
    }

    //    Result    //

    @JvmRecord
    data class Result(val items: Nel<HTItemResult>, val fluid: Optional<HTFluidResult>) {
        companion object {
            @JvmField
            val CODEC: Codec<Result> = HTCodecs.record { instance ->
                instance.group(
                    HTItemResult.CODEC
                        .compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 4)
                        .forGetter(Result::items),
                    HTFluidResult.CODEC.optionalFieldOf(HTConstants.FLUID).forGetter(Result::fluid),
                ).apply(instance, ::Result)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Result> = StreamCodec.composite(
                HTItemResult.STREAM_CODEC.nelOf(),
                Result::items,
                HTFluidResult.STREAM_CODEC.optionalOf(),
                Result::fluid,
                ::Result,
            )
        }

        fun create(): Output = Output(items.map(HTItemResult::create), fluid.createOrEmpty())
    }

    //    Basic    //

    open class Basic(val ingredient: HTItemIngredient, val result: Result, override val progressData: HTProgressData) :
        HTItemSeparatingRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemSeparatingRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    Result.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory::create)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTItemSeparatingRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                StreamCodec.composite(
                    HTItemIngredient.STREAM_CODEC,
                    Basic::ingredient,
                    Result.STREAM_CODEC,
                    Basic::result,
                    HTProgressData.STREAM_CODEC,
                    Basic::progressData,
                    factory::create,
                )
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun apply(input: ItemInstance): Output = result.create()

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

        override fun getProgressData(input: ItemInstance): HTProgressData = progressData
    }
}
