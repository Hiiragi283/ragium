package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

/**
 * 液体を別の液体とアイテムに変換するレシピ
 */
class HTRefiningRecipe(
    val ingredient: SizedFluidIngredient,
    val itemOutput: Optional<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRefiningRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTRefiningRecipe::ingredient),
                    HTItemOutput.CODEC.optionalFieldOf("item_output").forGetter(HTRefiningRecipe::itemOutput),
                    HTFluidOutput.CODEC
                        .listOf(1, 2)
                        .fieldOf("fluid_outputs")
                        .forGetter(HTRefiningRecipe::fluidOutputs),
                ).apply(instance, ::HTRefiningRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTRefiningRecipe> = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            HTRefiningRecipe::ingredient,
            HTItemOutput.STREAM_CODEC.toOptional(),
            HTRefiningRecipe::itemOutput,
            HTFluidOutput.STREAM_CODEC.listOf(),
            HTRefiningRecipe::fluidOutputs,
            ::HTRefiningRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getFluid(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
