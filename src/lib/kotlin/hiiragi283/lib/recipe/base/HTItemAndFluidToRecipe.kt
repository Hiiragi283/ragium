package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTItemAndFluidToRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
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
interface HTItemAndFluidToRecipe<OUTPUT : Any> : HTItemFluidToRecipe<OUTPUT> {
    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        val itemIngredient: HTItemIngredient,
        val fluidIngredient: HTFluidIngredient,
        val result: RESULT,
        final override val progressData: HTProgressData,
    ) : HTItemAndFluidToRecipe<OUTPUT>,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> codec(
                resultCodec: Codec<RESULT>,
                factory: HTItemAndFluidToRecipeBuilder.Factory<RESULT, RECIPE>,
            ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCodecs.mapPair(
                        HTItemIngredient.CODEC.fieldOf(HTConstants.ITEM),
                        HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID),
                    ).fieldOf(HTConstants.INGREDIENTS).forGetter { it.itemIngredient to it.fluidIngredient },
                    resultCodec.fieldOf(HTConstants.RESULT).forGetter(Basic<OUTPUT, RESULT>::result),
                    HTProgressRecipe.Holder.progressCodec(),
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> streamCodec(
                resultCodec: StreamCodec<in RegistryFriendlyByteBuf, RESULT>,
                factory: HTItemAndFluidToRecipeBuilder.Factory<RESULT, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::itemIngredient,
                HTFluidIngredient.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::fluidIngredient,
                resultCodec,
                Basic<OUTPUT, RESULT>::result,
                HTProgressData.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::progressData,
                factory::create,
            )
        }

        final override fun test(first: ItemInstance, second: FluidInstance): Boolean = itemIngredient.test(first) && fluidIngredient.test(second)

        final override fun apply(first: ItemInstance, second: FluidInstance): OUTPUT = result.create()

        final override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
            itemIngredient.getMatchingStack(first) to fluidIngredient.getMatchingStack(second)

        final override fun getProgressData(first: ItemInstance, second: FluidInstance): HTProgressData = progressData
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class BasicItem(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(itemIngredient, fluidIngredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(factory: HTItemAndFluidToRecipeBuilder.Factory<HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTItemResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemResult.STREAM_CODEC, factory)
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class BasicFluid(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTFluidResult, progressData: HTProgressData) :
        Basic<FluidStack, HTFluidResult>(itemIngredient, fluidIngredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(factory: HTItemAndFluidToRecipeBuilder.Factory<HTFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: HTItemAndFluidToRecipeBuilder.Factory<HTFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTFluidResult.STREAM_CODEC, factory)
        }
    }
}
