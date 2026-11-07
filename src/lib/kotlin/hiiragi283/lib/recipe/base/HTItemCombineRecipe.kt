package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.HTItemCombineRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.getItemOrEmpty
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.listOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemCombineRecipe : HTRecipeFactories.DoubleInput<RecipeInput, ItemInstance, ItemInstance, ItemStack> {
    val firstIngredient: HTIngredient<Item, ItemInstance>
    val secondIngredient: HTIngredient<Item, ItemInstance>

    override fun test(first: ItemInstance, second: ItemInstance): Boolean = firstIngredient.test(first) && secondIngredient.test(second)

    override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
        firstIngredient.getMatchingStack(first) to secondIngredient.getMatchingStack(second)

    override fun produce(input: RecipeInput): ItemStack = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

    override fun matches(input: RecipeInput): Boolean = test(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

    override fun getProgressData(input: RecipeInput): HTProgressData = getProgressData(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic(
        final override val firstIngredient: HTItemIngredient,
        final override val secondIngredient: HTItemIngredient,
        val result: HTItemResult,
        final override val progressData: HTProgressData,
    ) : HTItemCombineRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemCombineRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC
                        .listOf(2, 2)
                        .fieldOf(HTConstants.INGREDIENTS)
                        .forGetter { listOf(it.firstIngredient, it.secondIngredient) },
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressRecipe.Holder.progressCodec(),
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTItemCombineRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                StreamCodec.composite(
                    HTItemIngredient.STREAM_CODEC.listOf(),
                    { listOf(it.firstIngredient, it.secondIngredient) },
                    HTItemResult.STREAM_CODEC,
                    Basic::result,
                    HTProgressData.STREAM_CODEC,
                    Basic::progressData,
                    factory::create,
                )
        }

        final override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

        final override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()
    }
}
