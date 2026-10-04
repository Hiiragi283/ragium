package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance

interface RTReactingRecipe :
    HTRecipePredicates.DoubleFluid,
    HTRecipeFactories.DoubleFluid<HTItemAndFluidStack>,
    HTProgressRecipe<HTFluidRecipeInput> {

    @JvmRecord
    data class Basic(
        val primary: HTFluidIngredient,
        val secondary: HTFluidIngredient,
        val result: HTItemOrFluidResult,
        override val progressData: HTProgressData
    ) : RTReactingRecipe,
        HTProgressRecipe.Simple<HTFluidRecipeInput>,
        HTSerializableRecipe<HTFluidRecipeInput> {
        companion object {
            @JvmField
            val CODEC: MapCodec<Basic> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.PRIMARY_INGREDIENT).forGetter(Basic::primary),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.SECONDARY_INGREDIENT).forGetter(Basic::secondary),
                    HTItemOrFluidResult.CODEC.forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData)
                ).apply(instance, ::Basic)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Basic> = StreamCodec.composite(
                HTFluidIngredient.STREAM_CODEC,
                Basic::primary,
                HTFluidIngredient.STREAM_CODEC,
                Basic::secondary,
                HTItemOrFluidResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                ::Basic
            )

            @JvmField
            val SERIALIZER: RecipeSerializer<Basic> = RecipeSerializer(CODEC, STREAM_CODEC)
        }

        override fun test(first: FluidInstance, second: FluidInstance): Boolean =
            primary.test(first) && secondary.test(second)

        override fun apply(first: FluidInstance, second: FluidInstance): HTItemAndFluidStack = result.create()

        override fun getMatchingStack(first: FluidInstance, second: FluidInstance): Pair<FluidInstance, FluidInstance> =
            primary.getMatchingStack(first) to secondary.getMatchingStack(second)

        override fun getSerializer(): RecipeSerializer<Basic> = RagiumRecipeSerializers.REACTING

        override fun getType(): RecipeType<Basic> = RagiumRecipeTypes.REACTING
    }
}
