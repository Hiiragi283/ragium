package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTTripleItemToItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 2種類または3種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface HTTripleItemToItemRecipe :
    HTRecipePredicates.TripleItem,
    HTRecipeFactories.TripleItem<ItemStack>,
    HTProgressRecipe<RecipeInput> {

    fun asDoubleInput(): HTDoubleItemToItemRecipe? = null

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    open class Basic(
        val ingredient: HTItemIngredient,
        val extras: Nel<HTItemIngredient>,
        val result: HTItemResult,
        override val progressData: HTProgressData
    ) : HTTripleItemToItemRecipe,
        HTProgressRecipe.Simple<RecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTTripleItemToItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                        HTItemIngredient.CODEC.compactNelFieldOf("extra", "extras", 2).forGetter(Basic::extras),
                        HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                        HTProgressData.CODEC.forGetter(Basic::progressData)
                    ).apply(instance, factory::create)
                }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: HTTripleItemToItemRecipeBuilder.Factory<RECIPE>
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTItemIngredient.STREAM_CODEC.nelOf(),
                Basic::extras,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create
            )
        }

        val secondary: HTItemIngredient get() = extras.head
        val tertiary: HTItemIngredient? get() = extras.getOrNull(1)

        override fun asDoubleInput(): HTDoubleItemToItemRecipe? {
            if (tertiary != null) return null
            return object : HTDoubleItemToItemRecipe, HTProgressRecipe.Simple<RecipeInput> by this {
                override fun test(first: ItemInstance, second: ItemInstance): Boolean =
                    ingredient.test(first) && secondary.test(second)

                override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

                override fun getMatchingStack(
                    first: ItemInstance,
                    second: ItemInstance
                ): Pair<ItemInstance, ItemInstance> =
                    ingredient.getMatchingStack(first) to secondary.getMatchingStack(second)
            }
        }

        override fun test(first: ItemInstance, second: ItemInstance, third: ItemInstance): Boolean {
            if (!ingredient.test(first)) return false
            return when {
                secondary.test(second) -> tertiary?.test(third) ?: true
                secondary.test(third) -> tertiary?.test(second) ?: true
                else -> false
            }
        }

        override fun apply(first: ItemInstance, second: ItemInstance, third: ItemInstance): ItemStack = result.create()

        override fun getMatchingStack(
            first: ItemInstance,
            second: ItemInstance,
            third: ItemInstance
        ): Triple<ItemInstance, ItemInstance, ItemInstance> {
            val firstInput = ingredient.getMatchingStack(first)
            return when {
                secondary.test(second) -> Triple(
                    firstInput,
                    secondary.getMatchingStack(second),
                    tertiary?.getMatchingStack(third) ?: ItemStack.EMPTY
                )

                secondary.test(third) -> Triple(
                    firstInput,
                    (tertiary?.getMatchingStack(second) ?: ItemStack.EMPTY),
                    secondary.getMatchingStack(third)
                )

                else -> Triple(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)
            }
        }
    }
}
