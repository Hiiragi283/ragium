package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

abstract class HTBasicSingleExtraItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, val extra: Optional<HTItemResult>) :
    HTSingleExtraItemRecipe {
    final override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack)

    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        result.getStackOrNull(provider)

    final override fun getRequiredCount(): Int = ingredient.getRequiredAmount()

    final override fun assembleExtraItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        extra.getOrNull()?.getStackOrNull(provider)
}
