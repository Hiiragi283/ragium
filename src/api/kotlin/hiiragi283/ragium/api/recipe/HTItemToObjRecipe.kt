package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

interface HTItemToObjRecipe<R : HTRecipeResult<*, *>> : HTRecipe<SingleRecipeInput> {
    val ingredient: HTItemIngredient
    val result: R

    override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = test(input)

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY
}
