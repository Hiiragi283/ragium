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
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidInstance
import java.util.Optional

interface RTReactingRecipe :
    HTRecipePredicates.DoubleFluid,
    HTRecipeFactories.DoubleFluid<HTItemAndFluidResult>,
    HTProgressRecipe<HTFluidRecipeInput> {

    @JvmRecord
    data class Basic(
        val primary: HTFluidIngredient,
        val secondary: HTFluidIngredient,
        val itemResult: Optional<HTItemResult>,
        val fluidResult: HTFluidResult,
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
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.ITEM_RESULT).forGetter(Basic::itemResult),
                    HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT).forGetter(Basic::fluidResult),
                    HTProgressData.CODEC.forGetter(Basic::progressData)
                ).apply(instance, ::Basic)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Basic> = StreamCodec.composite(
                HTFluidIngredient.STREAM_CODEC,
                Basic::primary,
                HTFluidIngredient.STREAM_CODEC,
                Basic::secondary,
                ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
                Basic::itemResult,
                HTFluidResult.STREAM_CODEC,
                Basic::fluidResult,
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
            HTItemAndFluidResult(itemResult.createOrEmpty(), fluidResult.create())

        override fun getRequiredAmount(first: FluidInstance, second: FluidInstance): Pair<Int, Int> =
            primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

        override fun getSerializer(): RecipeSerializer<Basic> = RagiumRecipeSerializers.REACTING

        override fun getType(): HTRecipeType<Basic> = RagiumRecipeTypes.REACTING
    }
}
