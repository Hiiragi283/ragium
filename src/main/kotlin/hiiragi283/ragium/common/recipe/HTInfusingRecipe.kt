package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTInfusingRecipe(
    private val itemIngredient: SizedIngredient,
    private val fluidIngredient: SizedFluidIngredient,
    private val itemOutput: HTItemOutput?,
    private val fluidOutput: HTFluidOutput?,
) : HTMachineRecipe(RagiumRecipes.INFUSING) {
    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = itemIngredient.test(input.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = fluidIngredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = when {
        itemOutput == null && fluidOutput == null -> DataResult.error { "Either item or fluid output required!" }
        else -> DataResult.success(
            HTRecipeDefinition(
                listOf(itemIngredient),
                listOf(fluidIngredient),
                listOfNotNull(itemOutput),
                listOfNotNull(fluidOutput),
            ),
        )
    }
}
