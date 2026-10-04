package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance

class RTFreezingRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: HTItemCatalyst,
    val result: HTItemResult,
    override val progressData: HTProgressData
) : HTItemAndFluidToItemRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTFreezingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTFreezingRecipe::ingredient),
                HTItemCatalyst.CODEC.forGetter(RTFreezingRecipe::catalyst),
                HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTFreezingRecipe::result),
                HTProgressData.CODEC.forGetter(RTFreezingRecipe::progressData)
            ).apply(instance, ::RTFreezingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTFreezingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            RTFreezingRecipe::ingredient,
            HTItemCatalyst.STREAM_CODEC,
            RTFreezingRecipe::catalyst,
            HTItemResult.STREAM_CODEC,
            RTFreezingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTFreezingRecipe::progressData,
            ::RTFreezingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTFreezingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance): Boolean =
        catalyst.test(first) && ingredient.test(second)

    override fun apply(first: ItemInstance, second: FluidInstance): ItemStack = result.create()

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
        ItemStack.EMPTY to ingredient.getMatchingStack(second)

    override fun getSerializer(): RecipeSerializer<RTFreezingRecipe> = RagiumRecipeSerializers.FREEZING

    override fun getType(): RecipeType<RTFreezingRecipe> = RagiumRecipeTypes.FREEZING
}
