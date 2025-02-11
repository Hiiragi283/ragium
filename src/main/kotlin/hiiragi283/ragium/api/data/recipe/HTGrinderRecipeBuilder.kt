package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTGrinderRecipeBuilder : HTMachineRecipeBuilderBase<HTGrinderRecipeBuilder, HTGrinderRecipe>() {
    private var group: String? = null
    private lateinit var input: SizedIngredient
    private lateinit var output: HTItemResult

    override fun itemInput(ingredient: Ingredient, count: Int): HTGrinderRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTGrinderRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(result: HTItemResult): HTGrinderRecipeBuilder = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = result
    }

    override fun fluidOutput(stack: FluidStack): HTGrinderRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.getResultId()

    override val prefix: String = "grinder"

    override fun createRecipe(): HTGrinderRecipe = HTGrinderRecipe(
        group ?: "",
        input,
        this.output,
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
