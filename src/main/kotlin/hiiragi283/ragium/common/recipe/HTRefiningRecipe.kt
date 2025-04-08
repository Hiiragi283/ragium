package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * 液体を別の液体とアイテムに変換するレシピ
 */
class HTRefiningRecipe(
    val ingredient: SizedFluidIngredient,
    private val fluidOutput: HTFluidOutput,
    private val itemOutput: HTItemOutput?,
) : HTMachineRecipe(RagiumRecipes.REFINING) {
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
            listOf(fluidOutput),
        ),
    )
}
