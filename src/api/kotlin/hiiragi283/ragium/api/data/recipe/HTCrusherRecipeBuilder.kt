package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTCrusherRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTCrusherRecipeBuilder(lookup: HolderGetter<Item>) : HTMachineRecipeBuilderBase<HTCrusherRecipeBuilder, HTCrusherRecipe>(lookup) {
    private var group: String? = null
    private lateinit var input: HTItemIngredient
    private val outputs: MutableList<HTItemOutput> = mutableListOf()

    override fun itemInput(ingredient: HTItemIngredient): HTCrusherRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = ingredient
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTCrusherRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTCrusherRecipeBuilder = apply {
        outputs.add(output)
    }

    override fun fluidOutput(stack: FluidStack): HTCrusherRecipeBuilder = throw UnsupportedOperationException()

    override val prefix: String = "solidifier"

    override fun getPrimalId(): ResourceLocation = outputs[0].id

    override fun createRecipe(): HTCrusherRecipe = HTCrusherRecipe(group ?: "", input, outputs)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
