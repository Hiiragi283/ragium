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

class HTCrusherRecipe(group: String, val input: HTItemIngredient, itemOutputs: List<HTItemOutput>) :
    HTFluidOutputRecipe(group, itemOutputs, listOf()) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrusherRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.Companion.CODEC
                        .fieldOf("item_input")
                        .forGetter(HTCrusherRecipe::input),
                    HTRecipeCodecs.itemOutputs(1, 3),
                ).apply(instance, ::HTCrusherRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCrusherRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTCrusherRecipe::getGroup,
            HTItemIngredient.Companion.STREAM_CODEC,
            HTCrusherRecipe::input,
            HTItemOutput.Companion.STREAM_CODEC.toList(),
            HTCrusherRecipe::itemOutputs,
            ::HTCrusherRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean = input.test(context.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        validateItemOutput(context, 0)
        validateItemOutput(context, 1)
        validateItemOutput(context, 2)
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canExtract(input.count)) {
            throw HTMachineException.ShrinkItem()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        processItemOutput(context, 0)
        processItemOutput(context, 1)
        processItemOutput(context, 2)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(input.count, false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.CRUSHER
}
