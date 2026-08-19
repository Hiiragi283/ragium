package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTDoubleItemToItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTProgressRecipe<RecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(
        val primary: HTItemIngredient,
        val secondary: HTItemIngredient,
        val result: HTItemResult,
        override val progressData: HTProgressData,
    ) : HTDoubleItemToItemRecipe,
        HTProgressRecipe.Simple<RecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTDoubleItemToItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.PRIMARY_INGREDIENT).forGetter(Basic::primary),
                    HTItemIngredient.CODEC.fieldOf(HTConstants.SECONDARY_INGREDIENT).forGetter(Basic::secondary),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory::create)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTDoubleItemToItemRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::primary,
                HTItemIngredient.STREAM_CODEC,
                Basic::secondary,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create,
            )
        }

        override fun test(first: ItemInstance, second: ItemInstance): Boolean = primary.test(first) && secondary.test(second)

        override fun getRequiredAmount(first: ItemInstance, second: ItemInstance): Pair<Int, Int> = primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()
    }
}
