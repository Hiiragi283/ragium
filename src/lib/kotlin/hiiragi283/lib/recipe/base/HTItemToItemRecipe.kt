package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 1種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack>,
    HTProgressRecipe<SingleRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(val ingredient: HTItemIngredient, val result: HTItemResult, override val progressData: HTProgressData) :
        HTItemToItemRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemToItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory::create)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTItemToItemRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create,
            )
        }

        override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): ItemStack = result.createOrEmpty()
    }
}
