package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.listOrElement
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTRecipeDefinition(
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    val catalyst: Ingredient,
    val itemOutputs: List<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRecipeDefinition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .listOrElement()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTRecipeDefinition::itemInputs),
                    SizedFluidIngredient.FLAT_CODEC
                        .listOrElement()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTRecipeDefinition::fluidInputs),
                    Ingredient.CODEC
                        .optionalFieldOf("catalyst", Ingredient.EMPTY)
                        .forGetter(HTRecipeDefinition::catalyst),
                    HTItemOutput.CODEC
                        .listOrElement()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTRecipeDefinition::itemOutputs),
                    HTFluidOutput.CODEC
                        .listOrElement()
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
            Ingredient.CONTENTS_STREAM_CODEC,
            HTRecipeDefinition::catalyst,
            HTItemOutput.STREAM_CODEC.listOf(),
            HTRecipeDefinition::itemOutputs,
            HTFluidOutput.STREAM_CODEC.listOf(),
            HTRecipeDefinition::fluidOutputs,
            ::HTRecipeDefinition,
        )
    }

    constructor(
        itemInputs: List<SizedIngredient>,
        fluidInputs: List<SizedFluidIngredient>,
        itemOutputs: List<HTItemOutput>,
        fluidOutputs: List<HTFluidOutput>,
    ) : this(itemInputs, fluidInputs, Ingredient.EMPTY, itemOutputs, fluidOutputs)

    fun getItemIngredient(index: Int): Optional<SizedIngredient> = Optional.ofNullable(itemInputs.getOrNull(index))

    fun getFluidIngredient(index: Int): Optional<SizedFluidIngredient> = Optional.ofNullable(fluidInputs.getOrNull(index))

    fun getItemOutput(index: Int): Optional<HTItemOutput> = Optional.ofNullable(itemOutputs.getOrNull(index))

    fun getFluidOutput(index: Int): Optional<HTFluidOutput> = Optional.ofNullable(fluidOutputs.getOrNull(index))

    val isEmptyIngredient: Boolean get() = itemInputs.isEmpty() && fluidInputs.isEmpty()
    val isEmptyOutput: Boolean get() = itemOutputs.isEmpty() && fluidOutputs.isEmpty()
}
