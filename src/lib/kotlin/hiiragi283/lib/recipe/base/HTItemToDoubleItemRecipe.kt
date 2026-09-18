package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.nelOrElement
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 1種類のアイテムから2種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToDoubleItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<Pair<ItemStack, ItemStack>>,
    HTProgressRecipe<SingleRecipeInput> {

    fun asSingleOutput(): HTItemToItemRecipe = object : HTItemToItemRecipe {
        override fun test(input: ItemInstance): Boolean = this@HTItemToDoubleItemRecipe.test(input)

        override fun getRequiredAmount(input: ItemInstance): Int =
            this@HTItemToDoubleItemRecipe.getRequiredAmount(input)

        override fun apply(input: ItemInstance): ItemStack = this@HTItemToDoubleItemRecipe.apply(input).first

        override fun getProgressData(input: SingleRecipeInput): HTProgressData =
            this@HTItemToDoubleItemRecipe.getProgressData(input)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(
        val ingredient: HTItemIngredient,
        val result: Nel<HTItemResult>,
        override val progressData: HTProgressData
    ) : HTItemToDoubleItemRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemToDoubleItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                        HTItemResult.CODEC.nelOrElement(2).fieldOf(HTConstants.RESULTS).forGetter(Basic::result),
                        HTProgressData.CODEC.forGetter(Basic::progressData)
                    ).apply(instance, factory::create)
                }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: HTItemToDoubleItemRecipeBuilder.Factory<RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTItemResult.STREAM_CODEC.nelOf(),
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create
            )
        }

        override fun test(input: ItemInstance): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: ItemInstance): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): Pair<ItemStack, ItemStack> =
            result.head.create() to result.getOrNull(1).createOrEmpty()
    }
}
