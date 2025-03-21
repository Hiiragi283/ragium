package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTRefiningRecipe(val ingredient: SizedFluidIngredient, val itemOutput: HTItemOutput?, val fluidOutputs: List<HTFluidOutput>) :
    HTMachineRecipe(RagiumRecipes.REFINING) {
    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(),
            listOf(ingredient),
            listOfNotNull(itemOutput),
            fluidOutputs,
        ),
    )
}
