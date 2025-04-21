package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * アイテムを別のアイテムか液体に変換するレシピ
 */
class HTExtractingRecipe(
    private val ingredient: SizedIngredient,
    private val itemOutput: HTItemOutput?,
    private val fluidOutput: HTFluidOutput?,
) : HTMachineRecipe(RagiumRecipes.EXTRACTING) {
    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> {
        if (itemOutput == null && fluidOutput == null) {
            return DataResult.error { "Either one fluid or item output required!" }
        }
        return DataResult.success(
            HTRecipeDefinition(
                listOf(ingredient),
                listOf(),
                listOfNotNull(itemOutput),
                listOfNotNull(fluidOutput),
            ),
        )
    }
}
