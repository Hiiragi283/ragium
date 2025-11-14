package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.Optional

interface HTItemWithCatalystToItemRecipe : HTMultiInputsToObjRecipe {
    val ingredient: Optional<HTItemIngredient>
    val catalyst: HTItemIngredient
    val result: HTItemResult

    override fun test(input: HTMultiRecipeInput): Boolean {
        val stackIn: ItemStack = input.getItem(0)
        val bool1: Boolean = ingredient
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(stackIn) }
            .orElse(stackIn.isEmpty)
        val bool2: Boolean = catalyst.test(input.getItem(1))
        return bool1 && bool2
    }

    override fun assembleItem(input: HTMultiRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, registries, result)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool2: Boolean = catalyst.hasNoMatchingStacks()
        val bool3: Boolean = result.hasNoMatchingStack()
        return bool1 || bool2 || bool3
    }
}
