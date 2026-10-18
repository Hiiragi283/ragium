package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTSingleAndCatalystRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance

class RTFreezingRecipe(
    ingredient: HTFluidIngredient,
    catalyst: HTItemCatalyst,
    result: HTItemResult,
    progressData: HTProgressData
) : HTSingleAndCatalystRecipe<HTFluidIngredient, HTItemResult>(
    ingredient,
    catalyst,
    result,
    progressData
),
    HTItemAndFluidToItemRecipe,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTFreezingRecipe> = codec(HTFluidIngredient.CODEC, HTItemResult.CODEC, ::RTFreezingRecipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTFreezingRecipe> =
            streamCodec(HTFluidIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, ::RTFreezingRecipe)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTFreezingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance): Boolean =
        catalyst.test(first) && ingredient.test(second)

    override fun apply(first: ItemInstance, second: FluidInstance): ItemStack = result.create()

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
        ItemStack.EMPTY to ingredient.getMatchingStack(second)

    override fun getProgressData(first: ItemInstance, second: FluidInstance): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTFreezingRecipe> = RagiumRecipeSerializers.FREEZING

    override fun getType(): RecipeType<RTFreezingRecipe> = RagiumRecipeTypes.FREEZING
}
