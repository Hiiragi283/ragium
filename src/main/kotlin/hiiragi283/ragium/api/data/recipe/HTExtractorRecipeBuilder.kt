package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTExtractorRecipeBuilder : HTMachineRecipeBuilderBase<HTExtractorRecipeBuilder, HTExtractorRecipe>() {
    private var group: String? = null
    private lateinit var input: HTItemIngredient
    private var itemOutput: HTItemResult? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: HTItemIngredient): HTExtractorRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = ingredient
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTExtractorRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(result: HTItemResult): HTExtractorRecipeBuilder = apply {
        check(itemOutput == null) { "Output is already initialized" }
        this.itemOutput = result
    }

    override fun fluidOutput(stack: FluidStack): HTExtractorRecipeBuilder = apply {
        check(fluidOutput == null) { "Output is already initialized" }
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.getResultId()
        ?: fluidOutput?.fluidHolder?.idOrThrow
        ?: error("Either item or fluid output required!")

    override val prefix: String = "extractor"

    override fun createRecipe(): HTExtractorRecipe = HTExtractorRecipe(
        group ?: "",
        input,
        Optional.ofNullable(itemOutput),
        Optional.ofNullable(fluidOutput),
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
