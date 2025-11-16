package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput

/**
 * 複数のインプット（アイテム，液体）から複数の完成品（アイテム，液体）を生産するレシピ
 */
interface HTComplexRecipe :
    HTMultiInputsToObjRecipe,
    HTMultiOutputsRecipe<HTMultiRecipeInput>
