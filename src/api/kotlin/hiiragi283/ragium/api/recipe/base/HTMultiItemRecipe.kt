package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

/**
 * 複数のアイテムによるインプットを受け付けるレシピ
 */
abstract class HTMultiItemRecipe(
    group: String,
    val itemInputs: List<HTItemIngredient>,
    val fluidInput: Optional<SizedFluidIngredient>,
    val itemOutput: HTItemOutput,
) : HTMachineRecipe(group) {
    final override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = itemInputs[0].test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = itemInputs.getOrNull(1)?.test(context.getItemStack(HTStorageIO.INPUT, 1)) != false
        val bool3: Boolean = itemInputs.getOrNull(2)?.test(context.getItemStack(HTStorageIO.INPUT, 2)) != false
        val bool4: Boolean = fluidInput.map { it.test(context.getFluidStack(HTStorageIO.INPUT, 0)) }.orElse(true)
        return bool1 && bool2 && bool3 && bool4
    }

    final override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(itemOutput.get())) {
            throw HTMachineException.GrowItem()
        }
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canExtract(itemInputs[0].count)) {
            throw HTMachineException.ShrinkItem()
        }
        itemInputs.getOrNull(1)?.let { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 1).canExtract(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }
        itemInputs.getOrNull(2)?.let { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 2).canExtract(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }

        fluidInput.ifPresent { ingredient: SizedFluidIngredient ->
            if (!context.getTank(HTStorageIO.INPUT, 0).canExtract(ingredient.amount())) {
                throw HTMachineException.ShrinkFluid()
            }
        }
    }

    final override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(itemOutput.get(), false)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(itemInputs[0].count, false)
        itemInputs.getOrNull(1)?.let { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 1).extract(ingredient.count, false)
        }
        itemInputs.getOrNull(2)?.let { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 2).extract(ingredient.count, false)
        }

        fluidInput.ifPresent { ingredient: SizedFluidIngredient ->
            context.getTank(HTStorageIO.INPUT, 0).extract(ingredient.amount(), false)
        }
    }

    //    Serializer    //

    class Serializer<T : HTMultiItemRecipe>(
        private val factory: (String, List<HTItemIngredient>, Optional<SizedFluidIngredient>, HTItemOutput) -> T,
    ) : RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC
                        .listOf(1, 3)
                        .fieldOf("item_inputs")
                        .forGetter(HTMultiItemRecipe::itemInputs),
                    SizedFluidIngredient.FLAT_CODEC
                        .optionalFieldOf("fluid_input")
                        .forGetter(HTMultiItemRecipe::fluidInput),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTMultiItemRecipe::itemOutput),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTMultiItemRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC.toList(),
            HTMultiItemRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toOptional(),
            HTMultiItemRecipe::fluidInput,
            HTItemOutput.STREAM_CODEC,
            HTMultiItemRecipe::itemOutput,
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
