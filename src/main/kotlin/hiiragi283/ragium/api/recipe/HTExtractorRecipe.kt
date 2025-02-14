package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTExtractorRecipe(
    group: String,
    val input: HTItemIngredient,
    itemOutput: Optional<HTItemResult>,
    fluidOutput: Optional<FluidStack>,
) : HTFluidOutputRecipe(group, itemOutput, fluidOutput) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTExtractorRecipe::input),
                        HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTExtractorRecipe::itemOutput),
                        HTRecipeCodecs.FLUID_OUTPUT.forGetter(HTExtractorRecipe::fluidOutput),
                    ).apply(instance, ::HTExtractorRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractorRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTExtractorRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTExtractorRecipe::input,
            ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
            HTExtractorRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTExtractorRecipe::fluidOutput,
            ::HTExtractorRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input, 0)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTOR.get()
}
