package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class RTRefiningRecipe(
    val ingredient: HTFluidIngredient,
    val itemResult: Optional<HTItemResult>,
    val primary: HTFluidResult,
    val secondary: Optional<HTFluidResult>,
    override val progressData: HTProgressData
) : HTRecipePredicates.SingleFluid,
    HTRecipeFactories.SingleFluidTo<RTRefiningRecipe.RefiningResult>,
    HTProgressRecipe.Simple<HTSingleFluidRecipeInput>,
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTRefiningRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTFluidIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTRefiningRecipe::ingredient),
                HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM_RESULT).forGetter(RTRefiningRecipe::itemResult),
                HTFluidResult.CODEC.fieldOf(HTConstants.PRIMARY_RESULT).forGetter(RTRefiningRecipe::primary),
                HTFluidResult.CODEC.optionalFieldOf(
                    HTConstants.SECONDARY_RESULT
                ).forGetter(RTRefiningRecipe::secondary),
                HTProgressData.CODEC.forGetter(RTRefiningRecipe::progressData)
            ).apply(instance, ::RTRefiningRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTRefiningRecipe> = StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            RTRefiningRecipe::ingredient,
            HTStreamCodecs.optional(HTItemResult.STREAM_CODEC),
            RTRefiningRecipe::itemResult,
            HTFluidResult.STREAM_CODEC,
            RTRefiningRecipe::primary,
            HTStreamCodecs.optional(HTFluidResult.STREAM_CODEC),
            RTRefiningRecipe::secondary,
            HTProgressData.STREAM_CODEC,
            RTRefiningRecipe::progressData,
            ::RTRefiningRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTRefiningRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(input: FluidInstance): Boolean = ingredient.test(input)

    override fun apply(input: FluidInstance): RefiningResult =
        RefiningResult(primary.create(), secondary.createOrEmpty(), itemResult.createOrEmpty())

    override fun getRequiredAmount(input: FluidInstance): Int = ingredient.getRequiredAmount(input)

    override fun getSerializer(): RecipeSerializer<RTRefiningRecipe> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<RTRefiningRecipe> = RagiumRecipeTypes.REFINING

    @JvmRecord
    data class RefiningResult(val primary: FluidStack, val secondary: FluidStack, val item: ItemStack)
}
