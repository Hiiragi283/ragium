package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.HTFluidOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTMixerRecipe(
    group: String,
    val firstFluid: SizedFluidIngredient,
    val secondFluid: SizedFluidIngredient,
    itemOutput: Optional<HTItemResult>,
    fluidOutput: Optional<FluidStack>,
) : HTFluidOutputRecipe(group, itemOutput, fluidOutput) {
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
                        HTItemResult.CODEC.optionalFieldOf("item_output").forGetter(HTMixerRecipe::itemOutput),
                        FluidStack.CODEC.optionalFieldOf("fluid_output").forGetter(HTMixerRecipe::fluidOutput),
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
            ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
            HTMixerRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTMixerRecipe::fluidOutput,
            ::HTMixerRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean =
        firstFluid.test(input.getFluid(0)) && secondFluid.test(input.getFluid(1))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXER.get()
}
