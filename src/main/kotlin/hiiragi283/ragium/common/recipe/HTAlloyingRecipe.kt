package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTAlloyingRecipe(
    private val firstInput: SizedIngredient,
    private val secondInput: SizedIngredient,
    private val output: HTItemOutput,
) : HTMachineRecipe(RagiumRecipes.ALLOYING) {
    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = firstInput.test(input.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = secondInput.test(input.getItemStack(HTStorageIO.INPUT, 1))
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
            listOf(firstInput, secondInput),
            listOf(),
            listOf(output),
            listOf(),
        ),
    )
}
