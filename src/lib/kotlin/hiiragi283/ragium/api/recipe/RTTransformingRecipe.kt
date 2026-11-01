package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTEnchantmentPowerCondition
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class RTTransformingRecipe(
    val ingredient: HTItemIngredient,
    val condition: HTEnchantmentPowerCondition,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.DoubleInput<RTTransformingRecipe.Input, ItemInstance, Int>,
    HTRecipeFactories.DoubleInput<RTTransformingRecipe.Input, ItemInstance, Int, ItemStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<RTTransformingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTTransformingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTTransformingRecipe::ingredient),
                HTEnchantmentPowerCondition.CODEC.fieldOf(HTConstants.ENCHANTMENT_POWER).forGetter(RTTransformingRecipe::condition),
                HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTTransformingRecipe::result),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTTransformingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTTransformingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            RTTransformingRecipe::ingredient,
            HTEnchantmentPowerCondition.STREAM_CODEC,
            RTTransformingRecipe::condition,
            HTItemResult.STREAM_CODEC,
            RTTransformingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTTransformingRecipe::progressData,
            ::RTTransformingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTTransformingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: Int): Boolean = ingredient.test(first) && condition.test(second)

    override fun matches(input: Input): Boolean = test(input.item, input.power)

    override fun apply(first: ItemInstance, second: Int): ItemStack = result.create()

    override fun getMatchingStack(first: ItemInstance, second: Int): Pair<ItemInstance, Int> = ingredient.getMatchingStack(first) to 0

    override fun produce(input: Input): ItemStack = apply(input.item, input.power)

    override fun getProgressData(first: ItemInstance, second: Int): HTProgressData = progressData

    override fun getProgressData(input: Input): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTTransformingRecipe> = RagiumRecipeSerializers.TRANSFORMING

    override fun getType(): RecipeType<RTTransformingRecipe> = RagiumRecipeTypes.TRANSFORMING

    //    Input    //

    @JvmRecord
    data class Input(val item: ItemStack, val power: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> item
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 1
    }
}
