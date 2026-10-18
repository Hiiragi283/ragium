package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTSingleAndCatalystRecipe
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTEngravingRecipe(
    ingredient: HTItemIngredient,
    catalyst: HTItemCatalyst,
    result: HTItemResult,
    progressData: HTProgressData
) : HTSingleAndCatalystRecipe<HTItemIngredient, HTItemResult>(ingredient, catalyst, result, progressData),
    HTDoubleItemToItemRecipe,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTEngravingRecipe> = codec(HTItemIngredient.CODEC, HTItemResult.CODEC, ::RTEngravingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTEngravingRecipe> =
            streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, ::RTEngravingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTEngravingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: ItemInstance): Boolean =
        ingredient.test(first) && catalyst.test(second)

    override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

    override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
        ingredient.getMatchingStack(first) to catalyst.getMatchingStack(second)

    override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTEngravingRecipe> = RagiumRecipeSerializers.ENGRAVING

    override fun getType(): RecipeType<RTEngravingRecipe> = RagiumRecipeTypes.ENGRAVING
}
