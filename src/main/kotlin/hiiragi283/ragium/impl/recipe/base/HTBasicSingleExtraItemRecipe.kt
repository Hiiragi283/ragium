package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

abstract class HTBasicSingleExtraItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, val extra: Optional<HTItemResult>) :
    HTSingleExtraItemRecipe {
    final override fun assembleExtraItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, extra.getOrNull())

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, result)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.hasNoMatchingStacks()
        val bool2: Boolean = result.hasNoMatchingStack()
        return bool1 || bool2
    }

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
