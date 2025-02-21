package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTMixerRecipe(
    group: String,
    val firstFluid: SizedFluidIngredient,
    val secondFluid: SizedFluidIngredient,
    val itemInput: Optional<HTItemIngredient>,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMixerRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        SizedFluidIngredient.FLAT_CODEC
                            .fieldOf("first_fluidInput")
                            .forGetter(HTMixerRecipe::firstFluid),
                        SizedFluidIngredient.FLAT_CODEC
                            .fieldOf("second_fluidInput")
                            .forGetter(HTMixerRecipe::secondFluid),
                        HTItemIngredient.CODEC.optionalFieldOf("item_input").forGetter(HTMixerRecipe::itemInput),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 1),
                    ).apply(instance, ::HTMixerRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMixerRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMixerRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTMixerRecipe::firstFluid,
            SizedFluidIngredient.STREAM_CODEC,
            HTMixerRecipe::secondFluid,
            HTItemIngredient.STREAM_CODEC.toOptional(),
            HTMixerRecipe::itemInput,
            HTItemOutput.STREAM_CODEC.toList(),
            HTMixerRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTMixerRecipe::fluidOutputs,
            ::HTMixerRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput): Boolean {
        val bool1: Boolean = firstFluid.test(input.getFluid(0))
        val bool2: Boolean = secondFluid.test(input.getFluid(1))
        val bool3: Boolean = itemInput.map { it.test(input.getItem(0)) }.orElse(true)
        return bool1 && bool2 && bool3
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.MIXER
}
