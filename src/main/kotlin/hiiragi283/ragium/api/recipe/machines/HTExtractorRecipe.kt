package hiiragi283.ragium.api.recipe.machines

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.world.World

class HTExtractorRecipe(
    tier: HTMachineTier,
    val input: HTItemIngredient,
    override val itemOutputs: List<HTItemResult>,
    override val fluidOutputs: List<HTFluidResult>,
) : HTMachineRecipeBase<SingleStackRecipeInput>(RagiumMachineTypes.Processor.EXTRACTOR, tier) {
    override val itemInputs: List<HTItemIngredient> = listOf(input)
    override val fluidInputs: List<HTFluidIngredient> = listOf()
    override val catalyst: HTItemIngredient = HTItemIngredient.EMPTY_ITEM

    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }

    override fun getType(): RecipeType<*> {
        TODO("Not yet implemented")
    }

    override fun matches(input: SingleStackRecipeInput?, world: World?): Boolean {
        TODO("Not yet implemented")
    }
}
