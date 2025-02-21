package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTAssemblerRecipe(
    group: String,
    itemInputs: List<HTItemIngredient>,
    fluidInput: Optional<SizedFluidIngredient>,
    itemOutput: HTItemOutput,
) : HTMultiItemRecipe(group, itemInputs, fluidInput, itemOutput) {
    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ASSEMBLER
}
