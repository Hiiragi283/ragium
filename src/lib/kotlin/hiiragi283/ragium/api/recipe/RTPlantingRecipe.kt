package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTPlantingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTBasicSingleRecipe<HTItemIngredient, HTItemResult>(ingredient, result, progressData),
    HTItemToDoubleItemRecipe,
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTPlantingRecipe> = HTItemToRecipe.BasicItem.codec(::RTPlantingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTPlantingRecipe> =
            HTItemToRecipe.BasicItem.streamCodec(::RTPlantingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTPlantingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: ItemInstance): Boolean = ingredient.test(input)

    override fun apply(input: ItemInstance): Pair<ItemStack, ItemStack> =
        result.create() to getMatchingStack(input).let(HTIngredientHelper::unwrap).copy()

    override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

    override fun getSerializer(): RecipeSerializer<RTPlantingRecipe> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<RTPlantingRecipe> = RagiumRecipeTypes.PLANTING
}
