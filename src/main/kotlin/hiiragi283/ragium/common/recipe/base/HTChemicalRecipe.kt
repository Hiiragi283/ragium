package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

typealias HTChemicalIngredient = Ior<List<HTItemIngredient>, List<HTFluidIngredient>>
typealias HTChemicalResult = Ior<List<HTItemResult>, List<HTFluidResult>>

abstract class HTChemicalRecipe(val ingredients: HTChemicalIngredient, val results: HTChemicalResult, parameters: SubParameters) :
    HTProcessingRecipe<HTChemicalRecipeInput>(parameters) {
    override fun matches(input: HTChemicalRecipeInput, level: Level): Boolean = matchIngredients(input)

    protected fun matchIngredients(input: HTChemicalRecipeInput): Boolean = ingredients.fold(
        { HTShapelessRecipeHelper.shapelessMatch(it, input.items).isNotEmpty() },
        { HTShapelessRecipeHelper.shapelessMatch(it, input.fluids).isNotEmpty() },
        { items: List<HTItemIngredient>, fluids: List<HTFluidIngredient> ->
            val bool1: Boolean = HTShapelessRecipeHelper.shapelessMatch(items, input.items).isNotEmpty()
            val bool2: Boolean = HTShapelessRecipeHelper.shapelessMatch(fluids, input.fluids).isNotEmpty()
            bool1 && bool2
        },
    )

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
