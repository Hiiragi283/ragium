package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.recipe.HTChemicalRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.advancements.Criterion
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTChemicalRecipeBuilder : HTMachineRecipeBuilderBase<HTChemicalRecipeBuilder, HTChemicalRecipe>() {
    private var group: String? = null
    private val itemInputs: MutableMap<Ingredient, Int> = mutableMapOf()
    private val fluidInputs: MutableMap<FluidIngredient, Int> = mutableMapOf()
    private val itemOutputs: MutableList<ItemStack> = mutableListOf()
    private val fluidOutputs: MutableList<FluidStack> = mutableListOf()
    private var condition: HTMachineRecipeCondition? = null

    //    Input    //

    override fun itemInput(ingredient: Ingredient, count: Int): HTChemicalRecipeBuilder = apply {
        check(itemInputs.put(ingredient, count) == null) { "Same ingredient is not supported!" }
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTChemicalRecipeBuilder = apply {
        check(fluidInputs.put(ingredient, amount) == null) { "Same ingredient is not supported!" }
    }

    //    Machine Condition    //

    fun condition(condition: HTMachineRecipeCondition): HTChemicalRecipeBuilder = apply {
        check(this.condition == null) { "Machine condition is already registered!" }
        this.condition = condition
    }

    //    Output    //

    override fun itemOutput(stack: ItemStack): HTChemicalRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
        itemOutputs.add(stack.copy())
    }

    override fun fluidOutput(stack: FluidStack): HTChemicalRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty FluidStack is not allowed!" }
        fluidOutputs.add(stack.copy())
    }

    override fun getPrimalId(): ResourceLocation {
        val firstItem: Item? = itemOutputs.getOrNull(0)?.item
        if (firstItem != null) {
            return RecipeBuilder.getDefaultRecipeId(result)
        }
        val firstFluid: Fluid? = fluidOutputs.getOrNull(0)?.fluid
        if (firstFluid != null) {
            return BuiltInRegistries.FLUID.getKey(firstFluid)
        }
        return error("There is no output!")
    }

    override val prefix: String = "chemical"

    override fun createRecipe(): HTChemicalRecipe = HTChemicalRecipe(
        group ?: "",
        itemInputs.map { (ingredient: Ingredient, count: Int) ->
            SizedIngredient(ingredient, count)
        },
        fluidInputs.map { (ingredient: FluidIngredient, count: Int) ->
            SizedFluidIngredient(ingredient, count)
        },
        itemOutputs,
        fluidOutputs,
        Optional.ofNullable(condition),
    )

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = itemOutputs.getOrNull(0)?.item ?: error("Empty item outputs")
}
