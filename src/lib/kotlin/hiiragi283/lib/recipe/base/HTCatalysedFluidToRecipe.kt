package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTFluidWithCatalystRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類の液体と触媒から1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedFluidToFluidRecipe = HTCatalysedFluidToRecipe<FluidStack>

/**
 * 1種類の液体と触媒から1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedFluidToItemRecipe = HTCatalysedFluidToRecipe<ItemStack>

/**
 * 1種類の液体と触媒から1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedFluidToItemAndFluidRecipe = HTCatalysedFluidToRecipe<HTItemAndFluidStack>

/**
 * 1種類の液体と触媒から完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTCatalysedFluidToRecipe<OUTPUT : Any> : HTRecipeFactories.CatalysedSingleInput<HTFluidWithCatalystRecipeInput, FluidInstance, OUTPUT> {
    override fun matches(input: HTFluidWithCatalystRecipeInput): Boolean = test(input.fluid, input.catalyst)

    override fun produce(input: HTFluidWithCatalystRecipeInput): OUTPUT = apply(input.fluid, input.catalyst)

    override fun getProgressData(input: HTFluidWithCatalystRecipeInput): HTProgressData = getProgressData(input.fluid, input.catalyst)

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        ingredient: HTFluidIngredient,
        catalyst: HTItemCatalyst,
        result: RESULT,
        progressData: HTProgressData,
    ) : HTCatalysedSingleRecipe<HTFluidIngredient, RESULT>(ingredient, catalyst, result, progressData),
        HTCatalysedFluidToRecipe<OUTPUT> {
        final override fun getProgressData(first: FluidInstance, second: ItemInstance): HTProgressData = progressData

        final override fun test(input: FluidInstance, catalyst: ItemInstance): Boolean = ingredient.test(input) && this.catalyst.test(catalyst)

        final override fun apply(input: FluidInstance, catalyst: ItemInstance): OUTPUT = result.create()

        final override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class BasicItem(ingredient: HTFluidIngredient, catalyst: HTItemCatalyst, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(ingredient, catalyst, result, progressData) {
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
}
