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
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Ior
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance

interface RTReactingRecipe :
    HTRecipePredicates.DoubleFluid,
    HTRecipeFactories.DoubleFluid<HTItemAndFluidResult>,
    HTProgressRecipe<HTFluidRecipeInput> {

    @JvmRecord
    data class Basic(
        val primary: HTFluidIngredient,
        val secondary: HTFluidIngredient,
        val results: Ior<HTItemResult, HTFluidResult>,
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
                    HTCodecs.ior(
                        HTItemResult.CODEC.fieldOf(HTConstants.ITEM_RESULT),
                        HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT)
                    ).forGetter(Basic::results),
                    HTProgressData.CODEC.forGetter(Basic::progressData)
                ).apply(instance, ::Basic)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Basic> = StreamCodec.composite(
                HTFluidIngredient.STREAM_CODEC,
                Basic::primary,
                HTFluidIngredient.STREAM_CODEC,
                Basic::secondary,
                HTStreamCodecs.ior(HTItemResult.STREAM_CODEC, HTFluidResult.STREAM_CODEC),
                Basic::results,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                ::Basic
            )

            @JvmField
            val SERIALIZER: RecipeSerializer<Basic> = RecipeSerializer(CODEC, STREAM_CODEC)
        }

        override fun test(first: FluidInstance, second: FluidInstance): Boolean =
            primary.test(first) && secondary.test(second)

        override fun apply(first: FluidInstance, second: FluidInstance): HTItemAndFluidResult =
            results.mapLeft(HTItemResult::create).mapRight(HTFluidResult::create).let(::HTItemAndFluidResult)

        override fun getRequiredAmount(first: FluidInstance, second: FluidInstance): Pair<Int, Int> =
            primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

        override fun getSerializer(): RecipeSerializer<Basic> = RagiumRecipeSerializers.REACTING

        override fun getType(): HTRecipeType<Basic> = RagiumRecipeTypes.REACTING
    }
}
