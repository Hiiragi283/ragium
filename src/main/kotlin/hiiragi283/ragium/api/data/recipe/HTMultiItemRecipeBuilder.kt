package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.api.recipe.HTBlastFurnaceRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTMultiItemRecipeBuilder<T : HTMultiItemRecipe>(
    override val prefix: String,
    private val factory: (String, List<HTItemIngredient>, HTItemOutput) -> T,
) : HTMachineRecipeBuilderBase<HTMultiItemRecipeBuilder<T>, T>() {
    companion object {
        @JvmStatic
        fun assembler(): HTMultiItemRecipeBuilder<HTAssemblerRecipe> = HTMultiItemRecipeBuilder("assembler", ::HTAssemblerRecipe)

        @JvmStatic
        fun blastFurnace(): HTMultiItemRecipeBuilder<HTBlastFurnaceRecipe> =
            HTMultiItemRecipeBuilder("blast_furnace", ::HTBlastFurnaceRecipe)
    }

    private var group: String? = null
    private val itemInputs: MutableList<HTItemIngredient> = mutableListOf()
    private lateinit var output: HTItemOutput

    override fun itemInput(ingredient: HTItemIngredient): HTMultiItemRecipeBuilder<T> = apply {
        itemInputs.add(ingredient)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTMultiItemRecipeBuilder<T> = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(stack: FluidStack): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.id

    override fun createRecipe(): T = factory(group ?: "", itemInputs, this.output)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
