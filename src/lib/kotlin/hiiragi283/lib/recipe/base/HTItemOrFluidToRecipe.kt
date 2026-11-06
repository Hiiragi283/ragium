package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidToRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.toPair
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムまたは液体から1種類の液体を作成するレシピを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTItemOrFluidToFluidRecipe = HTItemOrFluidToRecipe<FluidStack>

/**
 * 1種類のアイテムまたは液体から1種類のアイテムを作成するレシピを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTItemOrFluidToItemRecipe = HTItemOrFluidToRecipe<ItemStack>

/**
 * 1種類のアイテムまたは液体から1種類のアイテムと液体を作成するレシピを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTItemOrFluidRecipe = HTItemOrFluidToRecipe<HTItemAndFluidStack>

/**
 * 1種類のアイテムまたは液体から1種類の完成品を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemOrFluidToRecipe<OUTPUT : Any> : HTItemFluidToRecipe<OUTPUT> {
    val ingredient: Either<HTIngredient<Item, ItemInstance>, HTIngredient<Fluid, FluidInstance>>

    override fun test(first: ItemInstance, second: FluidInstance): Boolean = ingredient.fold(
        { it.test(first) && HTIngredientHelper.isEmpty(second) },
        { HTIngredientHelper.isEmpty(first) && it.test(second) },
    )

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
        ingredient.mapLeft { it.getMatchingStack(first) }.map { it.getMatchingStack(second) }.toPair(ItemStack::EMPTY, FluidStack::EMPTY)

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic<OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>>(
        final override val ingredient: Either<HTItemIngredient, HTFluidIngredient>,
        val result: RESULT,
        final override val progressData: HTProgressData,
    ) : HTItemOrFluidToRecipe<OUTPUT>,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> codec(
                result: Codec<RESULT>,
                factory: HTItemOrFluidToRecipeBuilder.Factory<RESULT, RECIPE>,
            ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCodecs.xor(HTItemIngredient.CODEC, HTFluidIngredient.CODEC)
                        .fieldOf(HTConstants.INGREDIENT)
                        .forGetter(Basic<OUTPUT, RESULT>::ingredient),
                    result.fieldOf(HTConstants.RESULT).forGetter(Basic<OUTPUT, RESULT>::result),
                    HTProgressRecipe.Holder.progressCodec(),
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <OUTPUT : Any, RESULT : HTRecipeResult<OUTPUT>, RECIPE : Basic<OUTPUT, RESULT>> streamCodec(
                result: StreamCodec<in RegistryFriendlyByteBuf, RESULT>,
                factory: HTItemOrFluidToRecipeBuilder.Factory<RESULT, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTStreamCodecs.either(HTItemIngredient.STREAM_CODEC, HTFluidIngredient.STREAM_CODEC),
                Basic<OUTPUT, RESULT>::ingredient,
                result,
                Basic<OUTPUT, RESULT>::result,
                HTProgressData.STREAM_CODEC,
                Basic<OUTPUT, RESULT>::progressData,
                factory::create,
            )
        }

        final override fun getProgressData(first: ItemInstance, second: FluidInstance): HTProgressData = progressData

        final override fun apply(first: ItemInstance, second: FluidInstance): OUTPUT = result.create()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class BasicItem(ingredient: Either<HTItemIngredient, HTFluidIngredient>, result: HTItemResult, progressData: HTProgressData) :
        Basic<ItemStack, HTItemResult>(ingredient, result, progressData)

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class BasicItemAndFluid(ingredient: Either<HTItemIngredient, HTFluidIngredient>, result: HTItemOrFluidResult, progressData: HTProgressData) :
        Basic<HTItemAndFluidStack, HTItemOrFluidResult>(ingredient, result, progressData) {
        companion object {
            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> codec(factory: HTItemOrFluidToRecipeBuilder.Factory<HTItemOrFluidResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemOrFluidResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : BasicItemAndFluid> streamCodec(
                factory: HTItemOrFluidToRecipeBuilder.Factory<HTItemOrFluidResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemOrFluidResult.STREAM_CODEC, factory)
        }
    }
}
