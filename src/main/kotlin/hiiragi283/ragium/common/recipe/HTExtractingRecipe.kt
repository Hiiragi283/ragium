package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*
import kotlin.jvm.optionals.getOrNull

/**
 * アイテムを別のアイテムか液体に変換するレシピ
 */
class HTExtractingRecipe(
    private val ingredient: SizedIngredient,
    private val itemOutput: Optional<HTItemOutput>,
    private val fluidOutput: Optional<HTFluidOutput>,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractingRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTExtractingRecipe::ingredient),
                        HTItemOutput.CODEC.optionalFieldOf("item_output").forGetter(HTExtractingRecipe::itemOutput),
                        HTFluidOutput.CODEC.optionalFieldOf("fluid_output").forGetter(HTExtractingRecipe::fluidOutput),
                    ).apply(instance, ::HTExtractingRecipe)
            }.validate { recipe: HTExtractingRecipe ->
                if (recipe.itemOutput.isEmpty && recipe.fluidOutput.isEmpty) {
                    return@validate DataResult.error { "Either item or fluid output is required!" }
                }
                DataResult.success(recipe)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTExtractingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC.toOptional(),
            HTExtractingRecipe::itemOutput,
            HTFluidOutput.STREAM_CODEC.toOptional(),
            HTExtractingRecipe::fluidOutput,
            ::HTExtractingRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        // Item output
        val itemOutput: HTItemOutput? = this.itemOutput.getOrNull()
        if (itemOutput != null) {
            val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
            if (!outputSlot.canInsert(itemOutput.get())) return false
        }
        // Fluid output
        val fluidOutput: HTFluidOutput? = this.fluidOutput.getOrNull()
        if (fluidOutput != null) {
            val outputTank: HTFluidTank = input.getTankOrNull(HTStorageIO.OUTPUT, 0) ?: return false
            if (!outputTank.canInsert(fluidOutput.get())) return false
        }
        // Item input
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputSlot.canExtract(ingredient.count())
    }

    override fun process(input: HTMachineInput) {
        // Item output
        itemOutput.ifPresent { output: HTItemOutput ->
            input.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        }
        // Fluid output
        fluidOutput.ifPresent { output: HTFluidOutput ->
            input.getTank(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        }
        // Item input
        input.getSlot(HTStorageIO.INPUT, 0).extract(ingredient.count(), false)
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> {
        if (itemOutput.isEmpty && fluidOutput.isEmpty) {
            return DataResult.error { "Either one fluid or item output required!" }
        }
        return DataResult.success(
            HTRecipeDefinition(
                listOf(ingredient),
                listOf(),
                itemOutput.stream().toList(),
                fluidOutput.stream().toList(),
            ),
        )
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
