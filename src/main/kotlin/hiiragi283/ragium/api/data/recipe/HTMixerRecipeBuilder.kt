package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTMixerRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTMixerRecipeBuilder : HTMachineRecipeBuilderBase<HTMixerRecipeBuilder, HTMixerRecipe>() {
    private var group: String? = null
    private lateinit var firstInput: SizedFluidIngredient
    private lateinit var secondInput: SizedFluidIngredient
    private var itemOutput: HTItemResult? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTMixerRecipeBuilder = throw UnsupportedOperationException()

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMixerRecipeBuilder = apply {
        if (!::firstInput.isInitialized) {
            firstInput = SizedFluidIngredient(ingredient, amount)
            return@apply
        }
        check(!::secondInput.isInitialized) { "Input is already initialized" }
        secondInput = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(result: HTItemResult): HTMixerRecipeBuilder = apply {
        check(itemOutput == null) { "Output is already initialized" }
        this.itemOutput = result
    }

    override fun fluidOutput(stack: FluidStack): HTMixerRecipeBuilder = apply {
        check(fluidOutput == null) { "Output is already initialized" }
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.getResultId()
        ?: fluidOutput?.fluidHolder?.idOrThrow
        ?: error("Either item or fluid output required!")

    override val prefix: String = "mixer"

    override fun createRecipe(): HTMixerRecipe = HTMixerRecipe(
        group ?: "",
        firstInput,
        secondInput,
        Optional.ofNullable(itemOutput),
        Optional.ofNullable(fluidOutput),
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
