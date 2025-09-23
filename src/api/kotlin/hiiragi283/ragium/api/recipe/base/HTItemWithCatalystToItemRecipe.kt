package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.Optional

interface HTItemWithCatalystToItemRecipe : HTMultiItemToObjRecipe {
    val ingredient: Optional<HTItemIngredient>
    val catalyst: HTItemIngredient
    val result: HTItemResult

    override fun test(input: HTMultiItemRecipeInput): Boolean {
        val stackIn: ItemStack = input.getItem(0)
        val bool1: Boolean = ingredient
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(stackIn) }
            .orElse(stackIn.isEmpty)
        val bool2: Boolean = catalyst.test(input.getItem(1))
        return bool1 && bool2
    }

    override fun assemble(input: HTMultiItemRecipeInput, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, result)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool2: Boolean = catalyst.hasNoMatchingStacks()
        val bool3: Boolean = result.hasNoMatchingStack()
        return bool1 || bool2 || bool3
    }
}
