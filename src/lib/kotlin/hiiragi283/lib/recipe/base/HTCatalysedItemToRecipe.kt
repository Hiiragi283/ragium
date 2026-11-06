package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemWithCatalystRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムと触媒から1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedItemToFluidRecipe = HTCatalysedItemToRecipe<FluidStack>

/**
 * 1種類のアイテムと触媒から1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedItemToItemRecipe = HTCatalysedItemToRecipe<ItemStack>

/**
 * 1種類のアイテムと触媒から1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTCatalysedItemToItemAndFluidRecipe = HTCatalysedItemToRecipe<HTItemAndFluidStack>

/**
 * 1種類のアイテムと触媒から完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTCatalysedItemToRecipe<OUTPUT : Any> : HTRecipeFactories.CatalysedSingleInput<HTItemWithCatalystRecipeInput, ItemInstance, OUTPUT> {
    override fun matches(input: HTItemWithCatalystRecipeInput): Boolean = test(input.item, input.catalyst)

    override fun produce(input: HTItemWithCatalystRecipeInput): OUTPUT = apply(input.item, input.catalyst)

    override fun getProgressData(input: HTItemWithCatalystRecipeInput): HTProgressData = getProgressData(input.item, input.catalyst)

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        ingredient: HTItemIngredient,
        catalyst: HTItemCatalyst,
        result: RESULT,
        progressData: HTProgressData,
    ) : HTCatalysedSingleRecipe<HTItemIngredient, RESULT>(ingredient, catalyst, result, progressData),
        HTCatalysedItemToRecipe<OUTPUT> {
        final override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

        final override fun test(input: ItemInstance, catalyst: ItemInstance): Boolean = ingredient.test(input) && this.catalyst.test(catalyst)

        final override fun apply(input: ItemInstance, catalyst: ItemInstance): OUTPUT = result.create()

        final override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class BasicItem(ingredient: HTItemIngredient, catalyst: HTItemCatalyst, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(ingredient, catalyst, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(factory: Factory<HTItemIngredient, HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)
        }
    }
}
