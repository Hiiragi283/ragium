package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * 液体を別の液体に変換するレシピ
 */
abstract class HTSimpleFluidRecipe(
    recipeType: HTRecipeType<HTMachineInput, *>,
    private val ingredient: SizedFluidIngredient,
    private val output: HTFluidOutput,
) : HTMachineRecipe(recipeType) {
    final override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))

    final override fun canProcess(input: HTMachineInput): Boolean {
        // Fluid output
        val outputTank: HTFluidTank = input.getTankOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputTank.canInsert(output.get())) return false
        // Fluid input
        val inputTank: HTFluidTank = input.getTankOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputTank.canExtract(ingredient.amount())
    }

    final override fun process(input: HTMachineInput) {
        // Fluid output
        input.getTank(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        // Fluid input
        input.getTank(HTStorageIO.INPUT, 0).extract(ingredient.amount(), false)
    }

    final override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(),
            listOf(ingredient),
            listOf(),
            listOf(output),
        ),
    )
}
