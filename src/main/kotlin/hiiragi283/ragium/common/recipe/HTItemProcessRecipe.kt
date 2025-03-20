package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import net.neoforged.neoforge.common.crafting.SizedIngredient

abstract class HTItemProcessRecipe(
    recipeType: HTRecipeType<HTMachineInput, *>,
    private val ingredient: SizedIngredient,
    private val itemOutput: HTItemOutput,
    private val fluidOutput: HTFluidOutput?,
) : HTMachineRecipe(recipeType) {
    final override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    final override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    final override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    final override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(ingredient),
            listOf(),
            listOf(itemOutput),
            listOfNotNull(fluidOutput),
        ),
    )
}
