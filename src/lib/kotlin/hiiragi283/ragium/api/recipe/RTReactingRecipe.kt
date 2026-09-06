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
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.fold
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class RTReactingRecipe(
    val primary: HTFluidIngredient,
    val secondary: HTFluidIngredient,
    val itemResult: Optional<HTItemResult>,
    val fluidResult: HTFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicates.DoubleFluid,
    HTRecipeFactories.DoubleFluid<HTItemAndFluidResult>,
    HTProgressRecipe.Simple<HTFluidRecipeInput>,
    HTSerializableRecipe<HTFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC.fieldOf(HTConstants.PRIMARY_INGREDIENT).forGetter(RTReactingRecipe::primary),
                HTFluidIngredient.CODEC.fieldOf(
                    HTConstants.SECONDARY_INGREDIENT
                ).forGetter(RTReactingRecipe::secondary),
                HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM_RESULT).forGetter(RTReactingRecipe::itemResult),
                HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT).forGetter(RTReactingRecipe::fluidResult),
                HTProgressData.CODEC.forGetter(RTReactingRecipe::progressData)
            ).apply(instance, ::RTReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTReactingRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            RTReactingRecipe::primary,
            HTFluidIngredient.STREAM_CODEC,
            RTReactingRecipe::secondary,
            ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
            RTReactingRecipe::itemResult,
            HTFluidResult.STREAM_CODEC,
            RTReactingRecipe::fluidResult,
            HTProgressData.STREAM_CODEC,
            RTReactingRecipe::progressData,
            ::RTReactingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTReactingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: FluidInstance, second: FluidInstance): Boolean =
        primary.test(first) && secondary.test(second)

    override fun apply(first: FluidInstance, second: FluidInstance): HTItemAndFluidResult {
        val fluidStack: FluidStack = fluidResult.create()
        return itemResult.fold({ HTItemAndFluidResult(fluidStack) }, { HTItemAndFluidResult(it.create(), fluidStack) })
    }

    override fun getRequiredAmount(first: FluidInstance, second: FluidInstance): Pair<Int, Int> =
        primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<RTReactingRecipe> = RagiumRecipeSerializers.REACTING

    override fun getType(): HTRecipeType<RTReactingRecipe> = RagiumRecipeTypes.REACTING
}
