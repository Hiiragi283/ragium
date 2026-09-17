package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer

@JvmRecord
data class RTEnchantingRecipe(
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    override val progressData: HTProgressData
) : HTDoubleItemToItemRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val BOOK_INGREDIENT = HTItemIngredient(Ingredient.of(Items.BOOK), 1)

        @JvmField
        val CODEC: MapCodec<RTEnchantingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTEnchantingRecipe::ingredient),
                HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTEnchantingRecipe::result),
                HTProgressData.CODEC.forGetter(RTEnchantingRecipe::progressData)
            ).apply(instance, ::RTEnchantingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTEnchantingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            RTEnchantingRecipe::ingredient,
            HTItemResult.STREAM_CODEC,
            RTEnchantingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTEnchantingRecipe::progressData,
            ::RTEnchantingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTEnchantingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: ItemInstance): Boolean =
        BOOK_INGREDIENT.test(first) && ingredient.test(second)

    override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

    override fun getRequiredAmount(first: ItemInstance, second: ItemInstance): Pair<Int, Int> =
        BOOK_INGREDIENT.getRequiredAmount(first) to ingredient.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<RTEnchantingRecipe> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): HTRecipeType<RTEnchantingRecipe> = RagiumRecipeTypes.ENCHANTING
}
