package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Option
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTRefiningRecipe(
    val itemIngredient: Option<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val itemResult: Option<HTItemResult>,
    val fluidResult: HTFluidResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTItemAndFluidResult>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTRefiningRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.optionalFieldOf(HTConstants.ITEM_INGREDIENT).convert().forGetter(RTRefiningRecipe::itemIngredient),
                HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT).forGetter(RTRefiningRecipe::fluidIngredient),
                HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM_RESULT).convert().forGetter(RTRefiningRecipe::itemResult),
                HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT).forGetter(RTRefiningRecipe::fluidResult),
                HTProgressData.CODEC.forGetter(RTRefiningRecipe::progressData),
            ).apply(instance, ::RTRefiningRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTRefiningRecipe> = StreamCodec.composite(
            HTStreamCodecs.option(HTItemIngredient.STREAM_CODEC),
            RTRefiningRecipe::itemIngredient,
            HTFluidIngredient.STREAM_CODEC,
            RTRefiningRecipe::fluidIngredient,
            HTStreamCodecs.option(HTItemResult.STREAM_CODEC),
            RTRefiningRecipe::itemResult,
            HTFluidResult.STREAM_CODEC,
            RTRefiningRecipe::fluidResult,
            HTProgressData.STREAM_CODEC,
            RTRefiningRecipe::progressData,
            ::RTRefiningRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTRefiningRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance): Boolean = itemIngredient.fold({ HTIngredientHelper.isEmpty(first) }, { it.test(first) }) && fluidIngredient.test(second)

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = Pair(
        itemIngredient.fold({ 0 }, { it.getRequiredAmount(first) }),
        fluidIngredient.getRequiredAmount(second),
    )

    override fun apply(first: ItemInstance, second: FluidInstance): HTItemAndFluidResult {
        val stack: FluidStack = fluidResult.create()
        return itemResult.map(HTItemResult::create).fold({ HTItemAndFluidResult(stack) }, { HTItemAndFluidResult(it, stack) })
    }

    override fun getSerializer(): RecipeSerializer<RTRefiningRecipe> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<RTRefiningRecipe> = RagiumRecipeTypes.REFINING
}
