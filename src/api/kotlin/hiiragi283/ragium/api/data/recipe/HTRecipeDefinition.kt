package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTRecipeDefinition(
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    val catalyst: Ingredient,
    val itemOutputs: List<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) {
    fun getItemIngredient(index: Int): Optional<SizedIngredient> = Optional.ofNullable(itemInputs.getOrNull(index))

    fun getFluidIngredient(index: Int): Optional<SizedFluidIngredient> = Optional.ofNullable(fluidInputs.getOrNull(index))

    fun getItemOutput(index: Int): Optional<HTItemOutput> = Optional.ofNullable(itemOutputs.getOrNull(index))

    fun getFluidOutput(index: Int): Optional<HTFluidOutput> = Optional.ofNullable(fluidOutputs.getOrNull(index))
}
