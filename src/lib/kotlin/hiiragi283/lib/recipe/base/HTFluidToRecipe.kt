package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
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
 * 1種類の液体から1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
interface HTFluidToRecipe<OUTPUT : Any> :
    HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<OUTPUT>,
    HTProgressRecipe<HTSingleFluidRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicItem(ingredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
        HTBasicSingleRecipe<HTSingleFluidRecipeInput, HTFluidIngredient, HTItemResult>(
            ingredient,
            result,
            progressData
        ),
        HTFluidToRecipe<ItemStack> {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItem> codec(
                factory: Factory<HTFluidIngredient, HTItemResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTFluidIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItem> streamCodec(
                factory: Factory<HTFluidIngredient, HTItemResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                streamCodec(HTFluidIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicItem> = codec(::BasicItem)
        }

        override fun test(input: FluidInstance): Boolean = ingredient.test(input)

        override fun apply(input: FluidInstance): ItemStack = result.create()

        override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    open class BasicFluid(ingredient: HTFluidIngredient, result: HTFluidResult, progressData: HTProgressData) :
        HTBasicSingleRecipe<HTSingleFluidRecipeInput, HTFluidIngredient, HTFluidResult>(
            ingredient,
            result,
            progressData
        ),
        HTFluidToRecipe<FluidStack> {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicFluid> codec(
                factory: Factory<HTFluidIngredient, HTFluidResult, RECIPE>
            ): MapCodec<RECIPE> = codec(HTFluidIngredient.CODEC, HTFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicFluid> streamCodec(
                factory: Factory<HTFluidIngredient, HTFluidResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                streamCodec(HTFluidIngredient.STREAM_CODEC, HTFluidResult.STREAM_CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<BasicFluid> = codec(::BasicFluid)
        }

        override fun test(input: FluidInstance): Boolean = ingredient.test(input)

        override fun apply(input: FluidInstance): FluidStack = result.create()

        override fun getMatchingStack(input: FluidInstance): FluidInstance = ingredient.getMatchingStack(input)
    }
}
