package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTCentrifugingRecipe(
    val ingredient: Either<SizedIngredient, SizedFluidIngredient>,
    val itemOutputs: List<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) : HTMachineRecipe(TODO()) {
    override fun matches(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            ingredient.left().stream().toList(),
            ingredient.right().stream().toList(),
            itemOutputs,
            fluidOutputs,
        ),
    )
}
