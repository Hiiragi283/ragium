package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

/**
 * レシピ向けの[MapCodec]の一覧
 */
object HTRecipeCodecs {
    @JvmStatic
    fun <T : HTMachineRecipe> group(): RecordCodecBuilder<T, String> =
        Codec.STRING.optionalFieldOf("group", "").forGetter(HTMachineRecipe::getGroup)

    @JvmStatic
    fun <T : HTFluidOutputRecipe> itemOutputs(min: Int, max: Int): RecordCodecBuilder<T, List<HTItemOutput>> = HTItemOutput.CODEC
        .listOf(min, max)
        .optionalFieldOf("item_outputs", listOf())
        .forGetter { it.itemOutputs }

    @JvmStatic
    fun <T : HTFluidOutputRecipe> fluidOutputs(min: Int, max: Int): RecordCodecBuilder<T, List<HTFluidOutput>> = HTFluidOutput.CODEC
        .listOf(min, max)
        .optionalFieldOf("fluid_outputs", listOf())
        .forGetter { it.fluidOutputs }

    @JvmField
    val ITEM_INPUT: MapCodec<HTItemIngredient> = HTItemIngredient.CODEC.fieldOf("item_input")

    @JvmField
    val FLUID_INPUT: MapCodec<SizedFluidIngredient> = SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid_input")

    @JvmField
    val CATALYST: MapCodec<Optional<Ingredient>> = Ingredient.CODEC_NONEMPTY.optionalFieldOf("catalyst")

    @JvmField
    val ITEM_OUTPUT: MapCodec<HTItemOutput> = HTItemOutput.CODEC.fieldOf("item_output")
}
