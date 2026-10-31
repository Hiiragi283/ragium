package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
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
 * 1種類のアイテムから1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTItemToItemAndFluidRecipe = HTItemToRecipe<HTItemAndFluidStack>

/**
 * 1種類のアイテムから複数のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
typealias HTItemToMultiItemRecipe = HTItemToRecipe<List<ItemStack>>

/**
 * 1種類のアイテムから1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
interface HTItemToRecipe<OUTPUT : Any> :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<OUTPUT> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(ingredient: HTItemIngredient, result: RESULT, progressData: HTProgressData) :
        HTBasicSingleRecipe<HTItemIngredient, RESULT>(ingredient, result, progressData),
        HTItemToRecipe<OUTPUT> {
        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun apply(input: ItemInstance): OUTPUT = result.create()

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

        override fun getProgressData(input: ItemInstance): HTProgressData = progressData
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicItem(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(ingredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : HTBasicSingleRecipe<HTItemIngredient, HTItemResult>> codec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>,
            ): MapCodec<RECIPE> = codec(HTItemIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : HTBasicSingleRecipe<HTItemIngredient, HTItemResult>> streamCodec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicFluid(ingredient: HTItemIngredient, result: HTFluidResult, progressData: HTProgressData) :
        Basic<FluidStack, HTFluidResult>(
            ingredient,
            result,
            progressData,
        ) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(factory: Factory<HTItemIngredient, HTFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: Factory<HTItemIngredient, HTFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemIngredient.STREAM_CODEC, HTFluidResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    open class BasicItemAndFluid(ingredient: HTItemIngredient, result: HTItemOrFluidResult, progressData: HTProgressData) :
        Basic<HTItemAndFluidStack, HTItemOrFluidResult>(ingredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> codec(factory: Factory<HTItemIngredient, HTItemOrFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTItemOrFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> streamCodec(
                factory: Factory<HTItemIngredient, HTItemOrFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemIngredient.STREAM_CODEC, HTItemOrFluidResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class BasicMultiItem(val ingredient: HTItemIngredient, val results: Nel<HTItemResult>, override val progressData: HTProgressData) :
        HTItemToMultiItemRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicMultiItem> codec(factory: HTItemToMultiItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(BasicMultiItem::ingredient),
                        HTItemResult.CODEC
                            .compactNelFieldOf(HTConstants.RESULT, HTConstants.RESULTS, 4)
                            .forGetter(BasicMultiItem::results),
                        HTProgressRecipe.Holder.progressCodec(),
                    ).apply(instance, factory::create)
                }

            @JvmStatic
            fun <RECIPE : BasicMultiItem> streamCodec(
                factory: HTItemToMultiItemRecipeBuilder.Factory<RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                BasicMultiItem::ingredient,
                HTItemResult.STREAM_CODEC.nelOf(),
                BasicMultiItem::results,
                HTProgressData.STREAM_CODEC,
                BasicMultiItem::progressData,
                factory::create,
            )
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun getProgressData(input: ItemInstance): HTProgressData = progressData

        override fun apply(input: ItemInstance): List<ItemStack> = results.map(HTItemResult::create)

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)
    }
}
