package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

typealias HTChemicalIngredient = Ior<List<HTItemIngredient>, List<HTFluidIngredient>>
typealias HTChemicalResult = Ior<List<HTItemResult>, List<HTFluidResult>>

abstract class HTChemicalRecipe(val ingredients: HTChemicalIngredient, val results: HTChemicalResult, final override val time: Int) :
    HTProcessingRecipe.Serializable<HTChemicalRecipeInput> {
    protected fun matchIngredients(input: HTChemicalRecipeInput): Boolean = ingredients.map(
        { HTShapelessRecipeHelper.shapelessMatch(it, input.items).isNotEmpty() },
        { HTShapelessRecipeHelper.shapelessMatch(it, input.fluids).isNotEmpty() },
        { matchItems: Boolean, matchFluids: Boolean -> matchItems && matchFluids },
    )

    final override fun assemble(input: HTChemicalRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun test(input: HTChemicalRecipeInput): Boolean = matchIngredients(input)
}
