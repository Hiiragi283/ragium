package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムから1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
typealias HTItemToFluidRecipe = HTItemToRecipe<FluidStack>

/**
 * 1種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
typealias HTItemToItemRecipe = HTItemToRecipe<ItemStack>

/**
 * 1種類のアイテムから1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
interface HTItemToRecipe<OUTPUT : Any> :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<OUTPUT>,
    HTProgressRecipe<SingleRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        val ingredient: HTItemIngredient,
        val result: RESULT,
        override val progressData: HTProgressData
    ) : HTItemToRecipe<OUTPUT>,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        companion object {
            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> codec(
                resultCodec: Codec<RESULT>,
                factory: HTItemToRecipeBuilder.Factory<RESULT, RECIPE>
            ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(
                        HTConstants.INGREDIENT
                    ).forGetter(Basic<OUTPUT, RESULT>::ingredient),
                    resultCodec.fieldOf(HTConstants.RESULT).forGetter(Basic<OUTPUT, RESULT>::result),
                    HTProgressData.CODEC.forGetter(Basic<OUTPUT, RESULT>::progressData)
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> streamCodec(
                resultCodec: StreamCodec<in RegistryFriendlyByteBuf, RESULT>,
                factory: HTItemToRecipeBuilder.Factory<RESULT, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::ingredient,
                resultCodec,
                Basic<OUTPUT, RESULT>::result,
                HTProgressData.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::progressData,
                factory::create
            )
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: ItemInstance): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): OUTPUT = result.create()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicItem(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(ingredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(
                factory: HTItemToRecipeBuilder.Factory<HTItemResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: HTItemToRecipeBuilder.Factory<HTItemResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicItem> = codec(::BasicItem)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicFluid(ingredient: HTItemIngredient, result: HTFluidResult, progressData: HTProgressData) :
        Basic<FluidStack, HTFluidResult>(ingredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(
                factory: HTItemToRecipeBuilder.Factory<HTFluidResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: HTItemToRecipeBuilder.Factory<HTFluidResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicFluid> = codec(::BasicFluid)
        }
    }
}
