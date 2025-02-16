package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

/**
 * レシピ向けの[MapCodec]の一覧
 */
object HTRecipeCodecs {
    @JvmStatic
    fun <T : HTMachineRecipeBase> group(): RecordCodecBuilder<T, String> =
        Codec.STRING.optionalFieldOf("group", "").forGetter(HTMachineRecipeBase::getGroup)

    @JvmStatic
    fun <T : HTMachineRecipeBase> itemResult(): RecordCodecBuilder<T, HTItemOutput> =
        HTItemOutput.CODEC.fieldOf("item_output").forGetter { it.itemOutputs[0] }

    @JvmField
    val ITEM_INPUT: MapCodec<HTItemIngredient> = HTItemIngredient.CODEC.fieldOf("item_input")

    @JvmField
    val FLUID_INPUT: MapCodec<SizedFluidIngredient> = SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid_input")

    @JvmField
    val CATALYST: MapCodec<Optional<Ingredient>> = Ingredient.CODEC_NONEMPTY.optionalFieldOf("catalyst")

    @JvmField
    val ITEM_OUTPUT: MapCodec<Optional<HTItemOutput>> = HTItemOutput.CODEC.optionalFieldOf("item_output")

    @JvmField
    val FLUID_OUTPUT: MapCodec<Optional<FluidStack>> = FluidStack.CODEC.optionalFieldOf("fluid_output")
}
