package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.testOrEmpty
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance

@JvmRecord
data class RTReactingRecipe(
    val fluidIngredients: Nel<HTFluidIngredient>,
    val catalyst: HTItemCatalyst,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.ItemAndDoubleFluid,
    HTRecipeFactories.ItemAndDoubleFluid<HTItemAndFluidStack>,
    HTProgressRecipe.Simple<HTFluidRecipeInput>,
    HTSerializableRecipe<HTFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC
                    .compactNelFieldOf(HTConstants.INGREDIENT, HTConstants.INGREDIENTS, 2)
                    .forGetter(RTReactingRecipe::fluidIngredients),
                HTItemCatalyst.CODEC.forGetter(RTReactingRecipe::catalyst),
                HTItemOrFluidResult.CODEC.forGetter(RTReactingRecipe::result),
                HTProgressData.CODEC.forGetter(RTReactingRecipe::progressData)
            ).apply(instance, ::RTReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTReactingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC.nelOf(),
            RTReactingRecipe::fluidIngredients,
            HTItemCatalyst.STREAM_CODEC,
            RTReactingRecipe::catalyst,
            HTItemOrFluidResult.STREAM_CODEC,
            RTReactingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTReactingRecipe::progressData,
            ::RTReactingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTReactingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance, third: FluidInstance): Boolean = when {
        !catalyst.test(first) -> false
        fluidIngredients.head.test(second) && fluidIngredients.getOrNull(1).testOrEmpty(third) -> true
        else -> fluidIngredients.head.test(third) && fluidIngredients.getOrNull(1).testOrEmpty(second)
    }

    override fun apply(first: ItemInstance, second: FluidInstance, third: FluidInstance): HTItemAndFluidStack =
        result.create()

    override fun getMatchingStack(
        first: ItemInstance,
        second: FluidInstance,
        third: FluidInstance
    ): Triple<ItemInstance, FluidInstance, FluidInstance> {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<RTReactingRecipe> = RagiumRecipeSerializers.REACTING

    override fun getType(): RecipeType<RTReactingRecipe> = RagiumRecipeTypes.REACTING
}
