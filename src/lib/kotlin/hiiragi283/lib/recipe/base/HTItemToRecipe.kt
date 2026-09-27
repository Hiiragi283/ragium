package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
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
     * @since 26.1.5
     */
    open class BasicItem(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
        HTBasicSingleRecipe<SingleRecipeInput, HTItemIngredient, HTItemResult>(ingredient, result, progressData),
        HTItemToRecipe<ItemStack> {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(factory: Factory<HTItemIngredient, HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicItem> = codec(::BasicItem)
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun apply(input: ItemInstance): ItemStack = result.create()

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicFluid(ingredient: HTItemIngredient, result: HTFluidResult, progressData: HTProgressData) :
        HTBasicSingleRecipe<SingleRecipeInput, HTItemIngredient, HTFluidResult>(
            ingredient,
            result,
            progressData
        ),
        HTItemToRecipe<FluidStack> {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(
                factory: Factory<HTItemIngredient, HTFluidResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTItemIngredient.CODEC, HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: Factory<HTItemIngredient, HTFluidResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                streamCodec(HTItemIngredient.STREAM_CODEC, HTFluidResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicFluid> = codec(::BasicFluid)
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun apply(input: ItemInstance): FluidStack = result.create()

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)
    }
}
