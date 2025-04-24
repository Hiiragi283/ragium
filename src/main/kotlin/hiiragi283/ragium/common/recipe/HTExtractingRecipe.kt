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
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

/**
 * アイテムを別のアイテムか液体に変換するレシピ
 */
class HTExtractingRecipe(
    private val ingredient: SizedIngredient,
    private val itemOutput: Optional<HTItemOutput>,
    private val fluidOutput: Optional<HTFluidOutput>,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTExtractingRecipe::ingredient),
                    HTItemOutput.CODEC.optionalFieldOf("item_output").forGetter(HTExtractingRecipe::itemOutput),
                    HTFluidOutput.CODEC.optionalFieldOf("fluid_output").forGetter(HTExtractingRecipe::fluidOutput),
                ).apply(instance, ::HTExtractingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTExtractingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC.toOptional(),
            HTExtractingRecipe::itemOutput,
            HTFluidOutput.STREAM_CODEC.toOptional(),
            HTExtractingRecipe::fluidOutput,
            ::HTExtractingRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> {
        if (itemOutput.isEmpty && fluidOutput.isEmpty) {
            return DataResult.error { "Either one fluid or item output required!" }
        }
        return DataResult.success(
            HTRecipeDefinition(
                listOf(ingredient),
                listOf(),
                itemOutput.stream().toList(),
                fluidOutput.stream().toList(),
            ),
        )
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
