package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor

typealias HTComplexResult = Ior<HTItemResult, HTFluidResult>

fun Pair<HTItemResult?, HTFluidResult?>.toComplex(): HTComplexResult = this.toIor() ?: error("Either item or fluid result required")
