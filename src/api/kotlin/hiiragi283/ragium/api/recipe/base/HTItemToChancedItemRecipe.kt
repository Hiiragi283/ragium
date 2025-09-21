package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * アイテムのみを受け付ける[HTChancedItemRecipe]の拡張インターフェース
 */
interface HTItemToChancedItemRecipe : HTChancedItemRecipe<SingleRecipeInput> {
    /**
     * 液体を使わないので常に`0`を返す
     */
    override fun getIngredientAmount(input: SingleRecipeInput): Int = 0
}
