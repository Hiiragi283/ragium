package hiiragi283.ragium.api.recipe.chance

import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一のアイテムから複数の確率付きの完成品を生産するレシピ
 */
interface HTItemToChancedItemRecipe :
    HTChancedItemRecipe<SingleRecipeInput>,
    HTSingleItemRecipe
