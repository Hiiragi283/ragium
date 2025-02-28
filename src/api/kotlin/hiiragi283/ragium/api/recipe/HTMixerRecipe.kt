package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

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

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = firstFluid.test(context.getFluidStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = secondFluid.test(context.getFluidStack(HTStorageIO.INPUT, 1))
        val bool3: Boolean = itemInput.map { it.test(context.getItemStack(HTStorageIO.INPUT, 0)) }.orElse(true)
        return bool1 && bool2 && bool3
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        validateItemOutput(context, 0)
        validateFluidOutput(context, 0)
        // Input
        itemInput.ifPresent { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 0).canShrink(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }

        if (!context.getTank(HTStorageIO.INPUT, 0).canShrink(firstFluid.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
        if (!context.getTank(HTStorageIO.INPUT, 1).canShrink(secondFluid.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        processItemOutput(context, 0)
        processFluidOutput(context, 0)
        // Input
        itemInput.ifPresent { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 0).shrinkStack(ingredient.count, false)
        }

        context.getTank(HTStorageIO.INPUT, 0).shrinkStack(firstFluid.amount(), false)
        context.getTank(HTStorageIO.INPUT, 1).shrinkStack(secondFluid.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.MIXER
}
