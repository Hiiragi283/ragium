package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTDoubleItemToItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(
        val primary: HTItemIngredient,
        val secondary: HTItemIngredient,
        val result: HTItemResult,
        override val progressData: HTProgressData
    ) : HTDoubleItemToItemRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTDoubleItemToItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC.fieldOf("primary_ingredient").forGetter(Basic::primary),
                        HTItemIngredient.CODEC.fieldOf("secondary_ingredient").forGetter(Basic::secondary),
                        HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                        HTProgressData.CODEC.forGetter(Basic::progressData)
                    ).apply(instance, factory::create)
                }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: HTDoubleItemToItemRecipeBuilder.Factory<RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::primary,
                HTItemIngredient.STREAM_CODEC,
                Basic::secondary,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create
            )
        }

        override fun test(first: ItemInstance, second: ItemInstance): Boolean =
            primary.test(first) && secondary.test(second)

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

        override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
            primary.getMatchingStack(first) to secondary.getMatchingStack(second)

        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Catalysed(
        ingredient: HTItemIngredient,
        catalyst: HTItemCatalyst,
        result: HTItemResult,
        progressData: HTProgressData
    ) : HTCatalysedSingleRecipe<HTItemIngredient, HTItemResult>(ingredient, catalyst, result, progressData),
        HTDoubleItemToItemRecipe {
        companion object {
            @JvmStatic
            fun <RECIPE : Catalysed> codec(factory: Factory<HTItemIngredient, HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmField
            val SIMPLE_CODEC: MapCodec<Catalysed> = codec(::Catalysed)

            @JvmStatic
            fun <RECIPE : Catalysed> streamCodec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)
        }

        override fun test(first: ItemInstance, second: ItemInstance): Boolean =
            ingredient.test(first) && catalyst.test(second)

        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

        override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
            ingredient.getMatchingStack(first) to catalyst.getMatchingStack(second)
    }
}
