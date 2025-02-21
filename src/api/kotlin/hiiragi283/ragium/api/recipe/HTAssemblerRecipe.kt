package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTAssemblerRecipe(
    group: String,
    itemInputs: List<HTItemIngredient>,
    fluidInput: Optional<SizedFluidIngredient>,
    itemResult: HTItemOutput,
) : HTMultiItemRecipe(group, itemInputs, fluidInput, itemResult) {
    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ASSEMBLER
}
