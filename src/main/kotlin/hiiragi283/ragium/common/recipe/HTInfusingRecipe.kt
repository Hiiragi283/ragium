package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * アイテムと液体を別のアイテムに変換するレシピ
 */
class HTInfusingRecipe(
    private val itemIngredient: SizedIngredient,
    private val fluidIngredient: SizedFluidIngredient,
    private val output: HTItemOutput,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfusingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC.fieldOf("item_input").forGetter(HTInfusingRecipe::itemIngredient),
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid_input").forGetter(HTInfusingRecipe::fluidIngredient),
                    HTItemOutput.CODEC.fieldOf("output").forGetter(HTInfusingRecipe::output),
                ).apply(instance, ::HTInfusingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfusingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTInfusingRecipe::itemIngredient,
            SizedFluidIngredient.STREAM_CODEC,
            HTInfusingRecipe::fluidIngredient,
            HTItemOutput.STREAM_CODEC,
            HTInfusingRecipe::output,
            ::HTInfusingRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = itemIngredient.test(input.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = fluidIngredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(itemIngredient),
            listOf(fluidIngredient),
            listOf(output),
            listOf(),
        ),
    )

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSING.get()
}
