package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTMixerRecipe(
    group: String,
    val firstFluid: SizedFluidIngredient,
    val secondFluid: SizedFluidIngredient,
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
            HTItemOutput.STREAM_CODEC.toList(),
            HTMixerRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTMixerRecipe::fluidOutputs,
            ::HTMixerRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean =
        firstFluid.test(input.getFluid(0)) && secondFluid.test(input.getFluid(1))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXER.get()
}
