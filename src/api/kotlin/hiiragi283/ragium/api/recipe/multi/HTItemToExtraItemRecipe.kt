package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一のアイテムから主産物と副産物を生産するレシピ
 */
interface HTItemToExtraItemRecipe :
    HTExtraItemRecipe<SingleRecipeInput>,
    HTItemIngredient.CountGetter
