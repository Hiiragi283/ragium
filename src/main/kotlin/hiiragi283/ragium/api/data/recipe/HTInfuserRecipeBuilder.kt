package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTInfuserRecipeBuilder : HTMachineRecipeBuilderBase<HTInfuserRecipeBuilder, HTInfuserRecipe>() {
    private var group: String? = null
    private lateinit var itemInput1: HTItemIngredient
    private lateinit var fluidInput1: SizedFluidIngredient
    private var itemOutput: HTItemOutput? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: HTItemIngredient): HTInfuserRecipeBuilder = apply {
        check(!::itemInput1.isInitialized) { "Input is already initialized" }
        itemInput1 = ingredient
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTInfuserRecipeBuilder = apply {
        check(!::fluidInput1.isInitialized) { "Input is already initialized" }
        fluidInput1 = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(output: HTItemOutput): HTInfuserRecipeBuilder = apply {
        check(itemOutput == null) { "Output is already initialized" }
        this.itemOutput = output
    }

    override fun fluidOutput(stack: FluidStack): HTInfuserRecipeBuilder = apply {
        check(fluidOutput == null) { "Output is already initialized" }
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.id
        ?: fluidOutput?.fluidHolder?.idOrThrow
        ?: error("Either item or fluid output required!")

    override val prefix: String = "infuser"

    override fun createRecipe(): HTInfuserRecipe = HTInfuserRecipe(
        group ?: "",
        itemInput1,
        fluidInput1,
        Optional.ofNullable(itemOutput),
        Optional.ofNullable(fluidOutput),
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
