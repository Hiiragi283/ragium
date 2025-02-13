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

class HTRefineryRecipe(
    group: String,
    val input: SizedFluidIngredient,
    itemOutput: Optional<HTItemResult>,
    fluidOutput: Optional<FluidStack>,
) : HTFluidOutputRecipe(group, itemOutput, fluidOutput) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRefineryRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.FLUID_INPUT.forGetter(HTRefineryRecipe::input),
                        HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTRefineryRecipe::itemOutput),
                        HTRecipeCodecs.FLUID_OUTPUT.forGetter(HTRefineryRecipe::fluidOutput),
                    ).apply(instance, ::HTRefineryRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTRefineryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTRefineryRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTRefineryRecipe::input,
            ByteBufCodecs.optional(HTItemResult.STREAM_CODEC),
            HTRefineryRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTRefineryRecipe::fluidOutput,
            ::HTRefineryRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getFluid(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINERY.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINERY.get()
}
