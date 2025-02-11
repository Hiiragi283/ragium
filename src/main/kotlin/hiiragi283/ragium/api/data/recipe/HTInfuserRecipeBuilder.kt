package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTInfuserRecipeBuilder : HTMachineRecipeBuilderBase<HTInfuserRecipeBuilder, HTInfuserRecipe>() {
    private var group: String? = null
    private lateinit var itemInput1: SizedIngredient
    private lateinit var fluidInput1: SizedFluidIngredient
    private var itemOutput: HTItemResult? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTInfuserRecipeBuilder = apply {
        check(!::itemInput1.isInitialized) { "Input is already initialized" }
        itemInput1 = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTInfuserRecipeBuilder = apply {
        check(!::fluidInput1.isInitialized) { "Input is already initialized" }
        fluidInput1 = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(result: HTItemResult): HTInfuserRecipeBuilder = apply {
        check(itemOutput == null) { "Output is already initialized" }
        this.itemOutput = result
    }

    override fun fluidOutput(stack: FluidStack): HTInfuserRecipeBuilder = apply {
        check(fluidOutput == null) { "Output is already initialized" }
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.getResultId()
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
