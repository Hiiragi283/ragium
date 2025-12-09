package hiiragi283.ragium.api.recipe.extra

import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe

/**
 * 単一のアイテムから主産物と副産物を生産するレシピ
 */
interface HTSingleExtraItemRecipe :
    HTSingleItemRecipe,
    HTExtraItemRecipe
