package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.api.recipe.HTBlastFurnaceRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTMultiItemRecipeBuilder<T : HTMultiItemRecipe>(
    override val prefix: String,
    private val factory: (String, SizedIngredient, SizedIngredient, Optional<SizedIngredient>, HTItemResult) -> T,
) : HTMachineRecipeBuilderBase<HTMultiItemRecipeBuilder<T>, T>() {
    companion object {
        @JvmStatic
        fun assembler(): HTMultiItemRecipeBuilder<HTAssemblerRecipe> = HTMultiItemRecipeBuilder("assembler", ::HTAssemblerRecipe)

        @JvmStatic
        fun blastFurnace(): HTMultiItemRecipeBuilder<HTBlastFurnaceRecipe> =
            HTMultiItemRecipeBuilder("blast_furnace", ::HTBlastFurnaceRecipe)
    }

    private var group: String? = null
    private lateinit var firstInput: SizedIngredient
    private lateinit var secondInput: SizedIngredient
    private var thirdInput: SizedIngredient? = null
    private lateinit var output: HTItemResult

    override fun itemInput(ingredient: Ingredient, count: Int): HTMultiItemRecipeBuilder<T> = apply {
        if (!::firstInput.isInitialized) {
            this.firstInput = SizedIngredient(ingredient, count)
            return@apply
        }
        if (!::secondInput.isInitialized) {
            this.secondInput = SizedIngredient(ingredient, count)
            return@apply
        }
        check(thirdInput == null) { "Input is already initialized" }
        this.thirdInput = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(result: HTItemResult): HTMultiItemRecipeBuilder<T> = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = result
    }

    override fun fluidOutput(stack: FluidStack): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.getResultId()

    override fun createRecipe(): T = factory(group ?: "", firstInput, secondInput, Optional.ofNullable(thirdInput), this.output)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
