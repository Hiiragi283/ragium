package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTRefineryRecipe(
    group: String,
    val input: SizedFluidIngredient,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRefineryRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.FLUID_INPUT.forGetter(HTRefineryRecipe::input),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 2),
                    ).apply(instance, ::HTRefineryRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTRefineryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTRefineryRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTRefineryRecipe::input,
            HTItemOutput.STREAM_CODEC.toList(),
            HTRefineryRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTRefineryRecipe::fluidOutputs,
            ::HTRefineryRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean = this.input.test(context.getFluidStack(HTStorageIO.INPUT, 0))

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        validateItemOutput(context, 0)
        validateFluidOutput(context, 0)
        validateFluidOutput(context, 1)
        // Input
        if (!context.getTank(HTStorageIO.INPUT, 0).canExtract(input.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        processItemOutput(context, 0)
        processFluidOutput(context, 0)
        processFluidOutput(context, 1)
        // Input
        context.getTank(HTStorageIO.INPUT, 0).extract(input.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.REFINERY
}
