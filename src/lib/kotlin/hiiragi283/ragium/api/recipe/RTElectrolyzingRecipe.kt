package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.listOf
import hiiragi283.lib.util.Option
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTElectrolyzingRecipe(
    val itemIngredient: Option<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val results: List<HTFluidResult>,
    override val progressData: HTProgressData,
) : HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<Triple<FluidStack, FluidStack, FluidStack>>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTElectrolyzingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.optionalFieldOf(HTConstants.ITEM_INGREDIENT).convert().forGetter(RTElectrolyzingRecipe::itemIngredient),
                HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT).forGetter(RTElectrolyzingRecipe::fluidIngredient),
                HTFluidResult.CODEC.listOf(2, 3).fieldOf(HTConstants.RESULTS).forGetter(RTElectrolyzingRecipe::results),
                HTProgressData.CODEC.forGetter(RTElectrolyzingRecipe::progressData),
            ).apply(instance, ::RTElectrolyzingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTElectrolyzingRecipe> = StreamCodec.composite(
            HTStreamCodecs.option(HTItemIngredient.STREAM_CODEC),
            RTElectrolyzingRecipe::itemIngredient,
            HTFluidIngredient.STREAM_CODEC,
            RTElectrolyzingRecipe::fluidIngredient,
            HTFluidResult.STREAM_CODEC.listOf(),
            RTElectrolyzingRecipe::results,
            HTProgressData.STREAM_CODEC,
            RTElectrolyzingRecipe::progressData,
            ::RTElectrolyzingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTElectrolyzingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance): Boolean = itemIngredient.test(first) && fluidIngredient.test(second)

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = Pair(
        itemIngredient.fold({ 0 }, { it.getRequiredAmount(first) }),
        fluidIngredient.getRequiredAmount(second),
    )

    override fun apply(first: ItemInstance, second: FluidInstance): Triple<FluidStack, FluidStack, FluidStack> = Triple(
        results[0].create(),
        results[1].create(),
        results.getOrNull(2)?.create() ?: FluidStack.EMPTY,
    )

    override fun getSerializer(): RecipeSerializer<RTElectrolyzingRecipe> = RagiumRecipeSerializers.ELECTROLYZING

    override fun getType(): HTRecipeType<RTElectrolyzingRecipe> = RagiumRecipeTypes.ELECTROLYZING
}
