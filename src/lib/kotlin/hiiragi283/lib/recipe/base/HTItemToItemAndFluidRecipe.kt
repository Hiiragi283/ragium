package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 1種類のアイテムから1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToItemAndFluidRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<HTItemAndFluidResult>,
    HTProgressRecipe<SingleRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(
        val ingredient: HTItemIngredient,
        val itemResult: HTItemResult,
        val fluidResult: HTFluidResult,
        override val progressData: HTProgressData
    ) : HTItemToItemAndFluidRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(
                factory: (HTItemIngredient, HTItemResult, HTFluidResult, HTProgressData) -> RECIPE
            ): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.ITEM_RESULT).forGetter(Basic::itemResult),
                    HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT).forGetter(Basic::fluidResult),
                    HTProgressData.CODEC.forGetter(Basic::progressData)
                ).apply(instance, factory)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: (HTItemIngredient, HTItemResult, HTFluidResult, HTProgressData) -> RECIPE
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTItemResult.STREAM_CODEC,
                Basic::itemResult,
                HTFluidResult.STREAM_CODEC,
                Basic::fluidResult,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory
            )
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: ItemInstance): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): HTItemAndFluidResult =
            HTItemAndFluidResult(itemResult.create(), fluidResult.create())
    }
}
