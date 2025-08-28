package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

interface HTFluidTransformRecipe : HTFluidRecipe<HTItemWithFluidRecipeInput> {
    val fluidIngredient: HTFluidIngredient
    val itemIngredient: Optional<HTItemIngredient>
    val itemResult: Optional<HTItemResult>
    val fluidResult: Optional<HTFluidResult>

    override fun test(input: HTItemWithFluidRecipeInput): Boolean {
        val bool1: Boolean = fluidIngredient.test(input.fluid)
        val bool2: Boolean = itemIngredient
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(input.item) }
            .orElse(input.item.isEmpty)
        return !isIncomplete && bool1 && bool2
    }

    override fun assemble(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        itemResult.map { result: HTItemResult -> result.getOrEmpty(registries) }.orElse(ItemStack.EMPTY)

    override fun assembleFluid(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        if (test(input) && fluidResult.isPresent) fluidResult.get().getOrEmpty(registries) else FluidStack.EMPTY

    override fun isIncomplete(): Boolean {
        if (itemResult.isEmpty && fluidResult.isEmpty) return true
        val bool1: Boolean = fluidIngredient.hasNoMatchingStacks()
        val bool2: Boolean = itemIngredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool3: Boolean = itemResult.map(HTItemResult::hasNoMatchingStack).orElse(false)
        val bool4: Boolean = fluidResult.map(HTFluidResult::hasNoMatchingStack).orElse(false)
        return bool1 || bool2 || bool3 || bool4
    }
}
