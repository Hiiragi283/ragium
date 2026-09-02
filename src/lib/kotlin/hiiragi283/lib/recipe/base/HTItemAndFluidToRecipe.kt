package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemAndFluidToRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムと液体から1種類の液体を作成するレシピを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTItemAndFluidToFluidRecipe = HTItemAndFluidToRecipe<FluidStack>

/**
 * 1種類のアイテムと液体から1種類のアイテムを作成するレシピを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTItemAndFluidToItemRecipe = HTItemAndFluidToRecipe<ItemStack>

/**
 * 1種類のアイテムと液体から1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemAndFluidToRecipe<OUTPUT : Any> :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<OUTPUT>,
    HTProgressRecipe<HTItemAndFluidRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    abstract class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        val itemIngredient: HTCatalystOrIngredient,
        val fluidIngredient: HTFluidIngredient,
        val result: RESULT,
        override val progressData: HTProgressData
    ) : HTItemAndFluidToRecipe<OUTPUT>,
        HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
        companion object {
            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> codec(
                resultCodec: Codec<RESULT>,
                factory: HTItemAndFluidToRecipeBuilder.Factory<RESULT, RECIPE>
            ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCatalystOrIngredient.MAP_CODEC.forGetter(Basic<OUTPUT, RESULT>::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(
                        HTConstants.FLUID_INGREDIENT
                    ).forGetter(Basic<OUTPUT, RESULT>::fluidIngredient),
                    resultCodec.fieldOf(HTConstants.RESULT).forGetter(Basic<OUTPUT, RESULT>::result),
                    HTProgressData.CODEC.forGetter(Basic<OUTPUT, RESULT>::progressData)
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> streamCodec(
                resultCodec: StreamCodec<in RegistryFriendlyByteBuf, RESULT>,
                factory: HTItemAndFluidToRecipeBuilder.Factory<RESULT, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTCatalystOrIngredient.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::itemIngredient,
                HTFluidIngredient.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::fluidIngredient,
                resultCodec,
                Basic<OUTPUT, RESULT>::result,
                HTProgressData.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::progressData,
                factory::create
            )
        }

        override fun test(first: ItemInstance, second: FluidInstance): Boolean =
            itemIngredient.test(first) && fluidIngredient.test(second)

        override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> =
            itemIngredient.getRequiredAmount(first) to fluidIngredient.getRequiredAmount(second)

        override fun apply(first: ItemInstance, second: FluidInstance): OUTPUT = result.create()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class BasicItem(
        itemIngredient: HTCatalystOrIngredient,
        fluidIngredient: HTFluidIngredient,
        result: HTItemResult,
        progressData: HTProgressData
    ) : Basic<ItemStack, HTItemResult>(itemIngredient, fluidIngredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTItemResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTItemResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicItem> = codec(::BasicItem)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class BasicFluid(
        itemIngredient: HTCatalystOrIngredient,
        fluidIngredient: HTFluidIngredient,
        result: HTFluidResult,
        progressData: HTProgressData
    ) : Basic<FluidStack, HTFluidResult>(itemIngredient, fluidIngredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTFluidResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTFluidResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicFluid> = codec(::BasicFluid)
        }
    }
}
