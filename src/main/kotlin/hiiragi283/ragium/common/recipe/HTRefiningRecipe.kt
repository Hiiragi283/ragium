package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

/**
 * 液体を別の液体とアイテムに変換するレシピ
 */
class HTRefiningRecipe(
    private val ingredient: SizedFluidIngredient,
    private val fluidOutput: HTFluidOutput,
    private val itemOutput: Optional<HTItemOutput>,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRefiningRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTRefiningRecipe::ingredient),
                    HTFluidOutput.CODEC.fieldOf("fluid_output").forGetter(HTRefiningRecipe::fluidOutput),
                    HTItemOutput.CODEC.optionalFieldOf("item_output").forGetter(HTRefiningRecipe::itemOutput),
                ).apply(instance, ::HTRefiningRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTRefiningRecipe> = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC,
            HTRefiningRecipe::ingredient,
            HTFluidOutput.STREAM_CODEC,
            HTRefiningRecipe::fluidOutput,
            HTItemOutput.STREAM_CODEC.toOptional(),
            HTRefiningRecipe::itemOutput,
            ::HTRefiningRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(),
            listOf(ingredient),
            itemOutput.stream().toList(),
            listOf(fluidOutput),
        ),
    )

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
