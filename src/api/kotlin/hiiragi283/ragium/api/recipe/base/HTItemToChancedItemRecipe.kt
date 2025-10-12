package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * アイテムのみを受け付ける[HTChancedItemRecipe]の拡張インターフェース
 */
interface HTItemToChancedItemRecipe :
    HTChancedItemRecipe<SingleRecipeInput>,
    HTSingleInputRecipe
