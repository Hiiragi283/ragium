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

class HTInfuserRecipe(
    group: String,
    val itemInput: HTItemIngredient,
    val fluidInput: SizedFluidIngredient,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfuserRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTInfuserRecipe::itemInput),
                        HTRecipeCodecs.FLUID_INPUT.forGetter(HTInfuserRecipe::fluidInput),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 1),
                    ).apply(instance, ::HTInfuserRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfuserRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTInfuserRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTInfuserRecipe::itemInput,
            SizedFluidIngredient.STREAM_CODEC,
            HTInfuserRecipe::fluidInput,
            HTItemOutput.STREAM_CODEC.toList(),
            HTInfuserRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTInfuserRecipe::fluidOutputs,
            ::HTInfuserRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = itemInput.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = fluidInput.test(context.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        validateItemOutput(context, 0)
        validateFluidOutput(context, 0)
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canShrink(itemInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
        if (!context.getTank(HTStorageIO.INPUT, 0).canShrink(fluidInput.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        processItemOutput(context, 0)
        processFluidOutput(context, 0)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(itemInput.count, false)

        context.getTank(HTStorageIO.INPUT, 0).shrinkStack(fluidInput.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.INFUSER
}
