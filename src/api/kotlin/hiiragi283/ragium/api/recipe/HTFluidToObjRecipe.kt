package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

abstract class HTFluidToObjRecipe(
    private val recipeType: RecipeType<*>,
    val ingredient: HTFluidIngredient,
    val itemResult: Optional<HTItemResult>,
    val fluidResults: List<HTFluidResult>,
) : HTRecipe<HTSingleFluidRecipeInput>,
    HTFluidRecipe<HTSingleFluidRecipeInput> {
    final override fun test(input: HTSingleFluidRecipeInput): Boolean = !isIncomplete && ingredient.test(input.fluid)

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.hasNoMatchingStacks()
        val bool2: Boolean = itemResult.map(HTItemResult::hasNoMatchingStack).orElse(false)
        val bool3: Boolean = fluidResults.isEmpty()
        val bool4: Boolean = fluidResults.all(HTFluidResult::hasNoMatchingStack)
        return bool1 || bool2 || bool3 || bool4
    }

    final override fun assembleFluid(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        if (test(input)) fluidResults[0].getOrEmpty() else FluidStack.EMPTY

    final override fun assemble(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        itemResult.map(HTItemResult::getOrEmpty).orElse(ItemStack.EMPTY)

    final override fun getType(): RecipeType<*> = recipeType
}
