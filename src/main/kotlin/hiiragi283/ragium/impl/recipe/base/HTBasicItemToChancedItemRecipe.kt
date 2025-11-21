package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * [HTItemToChancedItemRecipe]の抽象クラス
 */
abstract class HTBasicItemToChancedItemRecipe(val ingredient: HTItemIngredient, override val results: List<HTItemResultWithChance>) :
    HTBasicChancedItemRecipe<SingleRecipeInput>(),
    HTItemToChancedItemRecipe {
    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks()
}
