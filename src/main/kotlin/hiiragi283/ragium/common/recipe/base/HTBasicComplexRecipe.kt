package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult

/**
 * [HTComplexRecipe]の抽象クラス
 */
abstract class HTBasicComplexRecipe(results: HTComplexResult) :
    HTBasicComplexOutputRecipe(results),
    HTComplexRecipe
