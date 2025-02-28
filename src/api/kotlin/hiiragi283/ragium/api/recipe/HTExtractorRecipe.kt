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

class HTExtractorRecipe(
    group: String,
    val input: HTItemIngredient,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTExtractorRecipe::input),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 1),
                    ).apply(instance, ::HTExtractorRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractorRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTExtractorRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTExtractorRecipe::input,
            HTItemOutput.STREAM_CODEC.toList(),
            HTExtractorRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTExtractorRecipe::fluidOutputs,
            ::HTExtractorRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean = this.input.test(context.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        validateItemOutput(context, 0)
        validateFluidOutput(context, 0)
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canShrink(input.count)) {
            throw HTMachineException.ShrinkItem()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        processItemOutput(context, 0)
        processFluidOutput(context, 0)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).shrinkStack(input.count, false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.EXTRACTOR
}
