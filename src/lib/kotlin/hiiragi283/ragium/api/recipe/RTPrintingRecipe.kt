package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTCatalysedItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemWithCatalystRecipeInput
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class RTPrintingRecipe(val ingredient: HTItemIngredient, val catalyst: HTItemCatalyst, override val progressData: HTProgressData) :
    HTCatalysedItemToItemRecipe,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<HTItemWithCatalystRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTPrintingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTPrintingRecipe::ingredient),
                HTItemCatalyst.CODEC.forGetter(RTPrintingRecipe::catalyst),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTPrintingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTPrintingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            RTPrintingRecipe::ingredient,
            HTItemCatalyst.STREAM_CODEC,
            RTPrintingRecipe::catalyst,
            HTProgressData.STREAM_CODEC,
            RTPrintingRecipe::progressData,
            ::RTPrintingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTPrintingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

    override fun test(input: ItemInstance, catalyst: ItemInstance): Boolean =
        ingredient.test(input) && catalyst.get(RagiumDataComponents.MEMORY_DISC_DATA)?.let(this.catalyst::testOnlyType) ?: false

    override fun apply(input: ItemInstance, catalyst: ItemInstance): ItemStack =
        catalyst.get(RagiumDataComponents.MEMORY_DISC_DATA)?.toStack() ?: ItemStack.EMPTY

    override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)

    override fun getSerializer(): RecipeSerializer<RTPrintingRecipe> = RagiumRecipeSerializers.PRINTING

    override fun getType(): RecipeType<RTPrintingRecipe> = RagiumRecipeTypes.PRINTING
}
