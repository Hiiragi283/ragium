package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.input.asList
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
data class RTMixingRecipe(
    val itemIngredients: Nel<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.DoubleInput<HTFluidRecipeInput, List<ItemInstance>, FluidInstance>,
    HTRecipeFactories.DoubleInput<HTFluidRecipeInput, List<ItemInstance>, FluidInstance, HTItemAndFluidStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<HTFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTMixingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTCodecs.mapPair(
                    HTItemIngredient.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 4),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID),
                ).fieldOf(HTConstants.INGREDIENTS).forGetter { it.itemIngredients to it.fluidIngredient },
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTMixingRecipe::result),
                HTProgressData.CODEC.forGetter(RTMixingRecipe::progressData),
            ).apply(instance, ::RTMixingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTMixingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC.nelOf(),
            RTMixingRecipe::itemIngredients,
            HTFluidIngredient.STREAM_CODEC,
            RTMixingRecipe::fluidIngredient,
            HTItemOrFluidResult.STREAM_CODEC,
            RTMixingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTMixingRecipe::progressData,
            ::RTMixingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTMixingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    private constructor(
        pair: Pair<Nel<HTItemIngredient>, HTFluidIngredient>,
        result: HTItemOrFluidResult,
        progressData: HTProgressData,
    ) : this(pair.first, pair.second, result, progressData)

    fun asSimple(): HTItemAndFluidToRecipe.BasicItemAndFluid =
        HTItemAndFluidToRecipe.BasicItemAndFluid(itemIngredients.head, fluidIngredient, this.result, this.progressData)

    override fun matches(input: HTFluidRecipeInput): Boolean = test(input.asList(), input.getFluidOrEmpty(0))

    override fun test(first: List<ItemInstance>, second: FluidInstance): Boolean =
        HTIngredientHelper.match(itemIngredients, first) && fluidIngredient.test(second)

    override fun produce(input: HTFluidRecipeInput): HTItemAndFluidStack = apply(input.asList(), input.getFluidOrEmpty(0))

    override fun apply(first: List<ItemInstance>, second: FluidInstance): HTItemAndFluidStack = result.create()

    override fun getMatchingStack(first: List<ItemInstance>, second: FluidInstance): Pair<List<ItemInstance>, FluidInstance> =
        HTIngredientHelper.getMatchingStacks(itemIngredients, first) to fluidIngredient.getMatchingStack(second)

    override fun getProgressData(input: HTFluidRecipeInput): HTProgressData = getProgressData(input.asList(), input.getFluidOrEmpty(0))

    override fun getProgressData(first: List<ItemInstance>, second: FluidInstance): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTMixingRecipe> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<RTMixingRecipe> = RagiumRecipeTypes.MIXING
}
