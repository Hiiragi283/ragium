package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTSizedShapelessRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.asList
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.listOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
interface HTSizedShapelessRecipe : HTRecipeFactories.SingleInput<RecipeInput, List<ItemInstance>, ItemStack> {
    override fun matches(input: RecipeInput): Boolean {
        val inputs: List<ItemStack> = input.asList()
        if (inputs.isEmpty()) return false
        return test(inputs)
    }

    override fun produce(input: RecipeInput): ItemStack {
        val inputs: List<ItemStack> = input.asList()
        if (inputs.isEmpty()) return ItemStack.EMPTY
        return apply(inputs)
    }

    override fun getProgressData(input: RecipeInput): HTProgressData {
        val inputs: List<ItemStack> = input.asList()
        if (inputs.isEmpty()) return HTProgressData.time(0)
        return getProgressData(inputs)
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Basic(val ingredients: List<HTItemIngredient>, val result: HTItemResult, override val progressData: HTProgressData) :
        HTSizedShapelessRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTSizedShapelessRecipeBuilder.Factory<RECIPE>, size: Int): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC
                            .listOf(2, size)
                            .fieldOf(HTConstants.INGREDIENTS)
                            .forGetter(Basic::ingredients),
                        HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                        HTProgressRecipe.Holder.progressCodec(),
                    ).apply(instance, factory::create)
                }

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTSizedShapelessRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                StreamCodec.composite(
                    HTItemIngredient.STREAM_CODEC.listOf(),
                    Basic::ingredients,
                    HTItemResult.STREAM_CODEC,
                    Basic::result,
                    HTProgressData.STREAM_CODEC,
                    Basic::progressData,
                    factory::create,
                )
        }

        override fun test(input: List<ItemInstance>): Boolean = HTIngredientHelper.match(ingredients, input)

        override fun apply(input: List<ItemInstance>): ItemStack = result.create()

        override fun getMatchingStack(input: List<ItemInstance>): List<ItemInstance> = HTIngredientHelper.getMatchingStacks(ingredients, input)

        override fun getProgressData(input: List<ItemInstance>): HTProgressData = progressData
    }
}
