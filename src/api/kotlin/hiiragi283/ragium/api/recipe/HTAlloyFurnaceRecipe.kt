package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class HTAlloyFurnaceRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: HTItemIngredient,
    val output: HTItemOutput,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAlloyFurnaceRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTAlloyFurnaceRecipe::firstInput),
                    HTItemIngredient.CODEC.fieldOf("second_item_input").forGetter(HTAlloyFurnaceRecipe::secondInput),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTAlloyFurnaceRecipe::output),
                ).apply(instance, ::HTAlloyFurnaceRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTAlloyFurnaceRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTAlloyFurnaceRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTAlloyFurnaceRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC,
            HTAlloyFurnaceRecipe::secondInput,
            HTItemOutput.STREAM_CODEC,
            HTAlloyFurnaceRecipe::output,
            ::HTAlloyFurnaceRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = firstInput.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = secondInput.test(context.getItemStack(HTStorageIO.INPUT, 1))
        return bool1 && bool2
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(output.get())) {
            throw HTMachineException.GrowItem()
        }

        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canExtract(firstInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
        if (!context.getSlot(HTStorageIO.INPUT, 1).canExtract(secondInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)

        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(firstInput.count, false)
        context.getSlot(HTStorageIO.INPUT, 1).extract(secondInput.count, false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ALLOY_FURNACE
}
