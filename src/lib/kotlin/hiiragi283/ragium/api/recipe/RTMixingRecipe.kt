package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.Ior
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class RTMixingRecipe(
    val ingredients: Ior<Nel<HTItemIngredient>, Nel<HTFluidIngredient>>,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicate<HTRecipeInput>,
    HTRecipeFactory<HTRecipeInput, HTItemAndFluidStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTMixingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTCodecs.ior(
                    HTItemIngredient.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 4),
                    HTFluidIngredient.CODEC.compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS, 2)
                ).fieldOf(HTConstants.INGREDIENT).forGetter(RTMixingRecipe::ingredients),
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTMixingRecipe::result),
                HTProgressData.CODEC.forGetter(RTMixingRecipe::progressData)
            ).apply(instance, ::RTMixingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTMixingRecipe> = StreamCodec.composite(
            HTStreamCodecs.ior(
                HTItemIngredient.STREAM_CODEC.nelOf(),
                HTFluidIngredient.STREAM_CODEC.nelOf()
            ),
            RTMixingRecipe::ingredients,
            HTItemOrFluidResult.STREAM_CODEC,
            RTMixingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTMixingRecipe::progressData,
            ::RTMixingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTMixingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    fun asSimple(): HTItemAndFluidToRecipe.BasicItemAndFluid? {
        val (item: HTItemIngredient?, fluid: HTFluidIngredient?) = this.ingredients
            .mapLeft { it.head }
            .mapRight { it.head }
            .toPair()
        return when {
            item == null || fluid == null -> null
            else -> HTItemAndFluidToRecipe.BasicItemAndFluid(item, fluid, this.result, this.progressData)
        }
    }

    override fun matches(input: HTRecipeInput): Boolean = ingredients.fold(
        { HTIngredientHelper.match(it, input.items) },
        { HTIngredientHelper.match(it, input.fluids) },
        { items: Nel<HTItemIngredient>, fluids: Nel<HTFluidIngredient> ->
            HTIngredientHelper.match(items, input.items) && HTIngredientHelper.match(fluids, input.fluids)
        }
    )

    override fun produce(input: HTRecipeInput): HTItemAndFluidStack = result.create()

    override fun getSerializer(): RecipeSerializer<RTMixingRecipe> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<RTMixingRecipe> = RagiumRecipeTypes.MIXING
}
