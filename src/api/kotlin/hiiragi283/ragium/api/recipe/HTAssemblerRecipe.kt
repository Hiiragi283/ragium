package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import java.util.*

class HTAssemblerRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: HTItemIngredient,
    val thirdInput: Optional<HTItemIngredient>,
    val output: HTItemOutput,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAssemblerRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTAssemblerRecipe::firstInput),
                    HTItemIngredient.CODEC.fieldOf("second_item_input").forGetter(HTAssemblerRecipe::secondInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("third_item_input")
                        .forGetter(HTAssemblerRecipe::thirdInput),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTAssemblerRecipe::output),
                ).apply(instance, ::HTAssemblerRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTAssemblerRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTAssemblerRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTAssemblerRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC,
            HTAssemblerRecipe::secondInput,
            ByteBufCodecs.optional(HTItemIngredient.STREAM_CODEC),
            HTAssemblerRecipe::thirdInput,
            HTItemOutput.STREAM_CODEC,
            HTAssemblerRecipe::output,
            ::HTAssemblerRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = firstInput.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = secondInput.test(context.getItemStack(HTStorageIO.INPUT, 1))
        val thirdStack: ItemStack = context.getItemStack(HTStorageIO.INPUT, 2)
        val bool3: Boolean = thirdInput.map { it.test(thirdStack) }.orElse(thirdStack.isEmpty)
        return bool1 && bool2 && bool3
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
        thirdInput.ifPresent { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 2).canExtract(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)

        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(firstInput.count, false)
        context.getSlot(HTStorageIO.INPUT, 1).extract(secondInput.count, false)
        thirdInput.ifPresent { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 2).extract(ingredient.count, false)
        }
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ASSEMBLER
}
