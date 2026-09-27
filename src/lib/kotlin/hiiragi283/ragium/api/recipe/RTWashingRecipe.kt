package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance

@JvmRecord
data class RTWashingRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    val results: Nel<HTItemResult>,
    override val progressData: HTProgressData
) : HTItemAndFluidToRecipe<Pair<ItemStack, ItemStack>>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTWashingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC
                    .fieldOf(HTConstants.ITEM_INGREDIENT)
                    .forGetter(RTWashingRecipe::itemIngredient),
                HTFluidIngredient.CODEC
                    .fieldOf(HTConstants.FLUID_INGREDIENT)
                    .forGetter(RTWashingRecipe::fluidIngredient),
                HTItemResult.CODEC
                    .compactNelFieldOf(HTConstants.RESULT, HTConstants.RESULTS, 2)
                    .forGetter(RTWashingRecipe::results),
                HTProgressData.CODEC.forGetter(RTWashingRecipe::progressData)
            ).apply(instance, ::RTWashingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTWashingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            RTWashingRecipe::itemIngredient,
            HTFluidIngredient.STREAM_CODEC,
            RTWashingRecipe::fluidIngredient,
            HTItemResult.STREAM_CODEC.nelOf(),
            RTWashingRecipe::results,
            HTProgressData.STREAM_CODEC,
            RTWashingRecipe::progressData,
            ::RTWashingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTWashingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance): Boolean =
        itemIngredient.test(first) && fluidIngredient.test(second)

    override fun apply(first: ItemInstance, second: FluidInstance): Pair<ItemStack, ItemStack> =
        results.head.create() to results.getOrNull(1).createOrEmpty()

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
        itemIngredient.getMatchingStack(first) to fluidIngredient.getMatchingStack(second)

    override fun getSerializer(): RecipeSerializer<RTWashingRecipe> = RagiumRecipeSerializers.WASHING

    override fun getType(): HTRecipeType<RTWashingRecipe> = RagiumRecipeTypes.WASHING
}
