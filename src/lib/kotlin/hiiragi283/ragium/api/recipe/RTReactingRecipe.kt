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
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance

@JvmRecord
data class RTReactingRecipe(
    val fluidIngredients: Nel<HTFluidIngredient>,
    val catalyst: HTItemCatalyst,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.DoubleInput<HTFluidRecipeInput, ItemInstance, List<FluidInstance>>,
    HTRecipeFactories.DoubleInput<HTFluidRecipeInput, ItemInstance, List<FluidInstance>, HTItemAndFluidStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<HTFluidRecipeInput> {
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

    override fun matches(input: HTFluidRecipeInput): Boolean = test(input.getItemOrEmpty(0), input.asFluidList())

    override fun test(first: ItemInstance, second: List<FluidInstance>): Boolean =
        catalyst.test(first) && HTIngredientHelper.match(fluidIngredients, second)

    override fun produce(input: HTFluidRecipeInput): HTItemAndFluidStack =
        apply(input.getItemOrEmpty(0), input.asFluidList())

    override fun apply(first: ItemInstance, second: List<FluidInstance>): HTItemAndFluidStack = result.create()

    override fun getMatchingStack(
        first: ItemInstance,
        second: List<FluidInstance>
    ): Pair<ItemInstance, List<FluidInstance>> =
        ItemStack.EMPTY to HTIngredientHelper.getMatchingStacks(fluidIngredients, second)

    override fun getProgressData(input: HTFluidRecipeInput): HTProgressData =
        getProgressData(input.getItemOrEmpty(0), input.asFluidList())

    override fun getProgressData(first: ItemInstance, second: List<FluidInstance>): HTProgressData = progressData

    override fun getSerializer(): RecipeSerializer<RTReactingRecipe> = RagiumRecipeSerializers.REACTING

    override fun getType(): RecipeType<RTReactingRecipe> = RagiumRecipeTypes.REACTING
}
