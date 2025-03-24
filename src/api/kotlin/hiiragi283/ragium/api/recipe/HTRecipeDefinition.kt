package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTRecipeDefinition(
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    val itemOutputs: List<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRecipeDefinition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTRecipeDefinition::itemInputs),
                    SizedFluidIngredient.FLAT_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTRecipeDefinition::fluidInputs),
                    HTItemOutput.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTRecipeDefinition::itemOutputs),
                    HTFluidOutput.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTRecipeDefinition::fluidOutputs),
                ).apply(instance, ::HTRecipeDefinition)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTRecipeDefinition> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.listOf(),
            HTRecipeDefinition::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.listOf(),
            HTRecipeDefinition::fluidInputs,
            HTItemOutput.STREAM_CODEC.listOf(),
            HTRecipeDefinition::itemOutputs,
            HTFluidOutput.STREAM_CODEC.listOf(),
            HTRecipeDefinition::fluidOutputs,
            ::HTRecipeDefinition,
        )
    }

    fun getItemIngredient(index: Int): SizedIngredient? = itemInputs.getOrNull(index)

    fun getFluidIngredient(index: Int): SizedFluidIngredient? = fluidInputs.getOrNull(index)

    fun getItemOutput(index: Int): HTItemOutput? = itemOutputs.getOrNull(index)

    fun getFluidOutput(index: Int): HTFluidOutput? = fluidOutputs.getOrNull(index)

    val isEmptyIngredient: Boolean get() = itemInputs.isEmpty() && fluidInputs.isEmpty()
    val isEmptyOutput: Boolean get() = itemOutputs.isEmpty() && fluidOutputs.isEmpty()
}
