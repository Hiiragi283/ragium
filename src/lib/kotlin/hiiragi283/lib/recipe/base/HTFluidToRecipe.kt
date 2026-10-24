package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類の液体から1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
typealias HTFluidToFluidRecipe = HTFluidToRecipe<FluidStack>

/**
 * 1種類の液体から1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
typealias HTFluidToItemRecipe = HTFluidToRecipe<ItemStack>

/**
 * 1種類の液体から1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTFluidToItemAndFluidRecipe = HTFluidToRecipe<HTItemAndFluidStack>

/**
 * 1種類の液体から1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
interface HTFluidToRecipe<OUTPUT : Any> :
    HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<OUTPUT> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(ingredient: HTFluidIngredient, result: RESULT, progressData: HTProgressData) :
        HTBasicSingleRecipe<HTFluidIngredient, RESULT>(ingredient, result, progressData),
        HTFluidToRecipe<OUTPUT> {
        override fun test(input: FluidInstance): Boolean = ingredient.test(input)

        override fun apply(input: FluidInstance): OUTPUT = result.create()

        override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)

        override fun getProgressData(input: FluidInstance): HTProgressData = progressData
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicItem(ingredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(
            ingredient,
            result,
            progressData,
        ) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(factory: Factory<HTFluidIngredient, HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTFluidIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: Factory<HTFluidIngredient, HTItemResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicFluid(ingredient: HTFluidIngredient, result: HTFluidResult, progressData: HTProgressData) :
        Basic<FluidStack, HTFluidResult>(
            ingredient,
            result,
            progressData,
        ) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(factory: Factory<HTFluidIngredient, HTFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTFluidIngredient.CODEC, HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: Factory<HTFluidIngredient, HTFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidIngredient.STREAM_CODEC, HTFluidResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    open class BasicItemAndFluid(ingredient: HTFluidIngredient, result: HTItemOrFluidResult, progressData: HTProgressData) :
        Basic<HTItemAndFluidStack, HTItemOrFluidResult>(
            ingredient,
            result,
            progressData,
        ) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> codec(factory: Factory<HTFluidIngredient, HTItemOrFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTFluidIngredient.CODEC, HTItemOrFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> streamCodec(
                factory: Factory<HTFluidIngredient, HTItemOrFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidIngredient.STREAM_CODEC, HTItemOrFluidResult.STREAM_CODEC, factory)
        }
    }
}
