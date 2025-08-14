package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

interface HTItemWithFluidToObjRecipe<R : HTRecipeResult<*, *>> : HTRecipe<HTItemWithFluidRecipeInput> {
    val itemIngredient: Optional<HTItemIngredient>
    val fluidIngredient: Optional<HTFluidIngredient>
    val result: R

    override fun test(input: HTItemWithFluidRecipeInput): Boolean {
        val bool1: Boolean = itemIngredient
            .map { ingredient: HTItemIngredient -> testItem(ingredient, input.item) }
            .orElse(input.item.isEmpty)
        val bool2: Boolean = fluidIngredient
            .map { ingredient: HTFluidIngredient -> testFluid(ingredient, input.fluid) }
            .orElse(input.fluid.isEmpty)
        return !isIncomplete && bool1 && bool2
    }

    fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean

    fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean

    override fun assemble(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = itemIngredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(true)
        val bool2: Boolean = fluidIngredient.map(HTFluidIngredient::hasNoMatchingStacks).orElse(true)
        val bool3: Boolean = itemIngredient.isEmpty && fluidIngredient.isEmpty
        return bool1 || bool2 || bool3
    }
}
