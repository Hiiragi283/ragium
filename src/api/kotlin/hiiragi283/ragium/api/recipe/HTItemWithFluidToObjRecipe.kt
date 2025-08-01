package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

abstract class HTItemWithFluidToObjRecipe<R : HTRecipeResult<*, *>>(
    private val recipeType: RecipeType<*>,
    val itemIngredient: Optional<HTItemIngredient>,
    val fluidIngredient: Optional<HTFluidIngredient>,
    val result: R,
) : HTRecipe<HTItemWithFluidRecipeInput> {
    final override fun test(input: HTItemWithFluidRecipeInput): Boolean {
        val bool1: Boolean = itemIngredient
            .map { ingredient: HTItemIngredient -> testItem(ingredient, input.item) }
            .orElse(input.item.isEmpty)
        val bool2: Boolean = fluidIngredient
            .map { ingredient: HTFluidIngredient -> testFluid(ingredient, input.fluid) }
            .orElse(input.fluid.isEmpty)
        return !isIncomplete && bool1 && bool2
    }

    abstract fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean

    abstract fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean

    final override fun matches(input: HTItemWithFluidRecipeInput, level: Level): Boolean = test(input)

    final override fun getType(): RecipeType<*> = recipeType
}
