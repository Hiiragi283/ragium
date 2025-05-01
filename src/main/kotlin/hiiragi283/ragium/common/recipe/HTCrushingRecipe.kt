package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
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
 * アイテムを別のアイテムに変換するレシピ
 */
class HTCrushingRecipe(
    private val ingredient: SizedIngredient,
    private val output: HTItemOutput,
    private val secondOutput: Optional<HTItemOutput>,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTCrushingRecipe::ingredient),
                    HTItemOutput.CODEC.fieldOf("output").forGetter(HTCrushingRecipe::output),
                    HTItemOutput.CODEC.optionalFieldOf("second_output").forGetter(HTCrushingRecipe::secondOutput),
                ).apply(instance, ::HTCrushingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTCrushingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC,
            HTCrushingRecipe::output,
            HTItemOutput.STREAM_CODEC.toOptional(),
            HTCrushingRecipe::secondOutput,
            ::HTCrushingRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        // Item output
        val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputSlot.canInsert(output.get())) return false
        // Item input
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputSlot.canExtract(ingredient.count())
    }

    override fun process(input: HTMachineInput) {
        // Item output
        input.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        // Second Item output
        secondOutput.ifPresent { output: HTItemOutput ->
            input.getSlotOrNull(HTStorageIO.OUTPUT, 1)?.insert(output.get(), false)
        }
        // Item input
        input.getSlot(HTStorageIO.INPUT, 0).extract(ingredient.count(), false)
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(ingredient),
            listOf(),
            listOfNotNull(output, secondOutput.getOrNull()),
            listOf(),
        ),
    )

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
