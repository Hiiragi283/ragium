package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class RTReactingRecipe(
    val fluidIngredients: Nel<HTFluidIngredient>,
    val catalyst: HTItemCatalyst,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicate<HTRecipeInput>,
    HTRecipeFactory<HTRecipeInput, HTItemAndFluidStack>,
    HTProgressRecipe.Simple<HTRecipeInput>,
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC
                    .compactNelFieldOf(HTConstants.INGREDIENT, HTConstants.INGREDIENTS, 2)
                    .forGetter(RTReactingRecipe::fluidIngredients),
                HTItemCatalyst.CODEC.forGetter(RTReactingRecipe::catalyst),
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTReactingRecipe::result),
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

    override fun matches(input: HTRecipeInput): Boolean =
        catalyst.test(input.getItemOrEmpty(0)) && HTIngredientHelper.match(fluidIngredients, input.fluids)

    override fun produce(input: HTRecipeInput): HTItemAndFluidStack = result.create()

    override fun getSerializer(): RecipeSerializer<RTReactingRecipe> = RagiumRecipeSerializers.REACTING

    override fun getType(): RecipeType<RTReactingRecipe> = RagiumRecipeTypes.REACTING
}
