package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.toComplex
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

abstract class HTAbstractComplexRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    val result = ResultHolder()

    inner class ResultHolder : HTIdLike {
        private var itemResult: HTItemResult? = null
        private var fluidResult: HTFluidResult? = null

        operator fun plusAssign(result: HTItemResult?) {
            check(this.itemResult == null) { "Item result already initialized!" }
            this.itemResult = result
        }

        operator fun plusAssign(result: HTFluidResult?) {
            check(this.fluidResult == null) { "Fluid result already initialized!" }
            this.fluidResult = result
        }

        fun build(): HTComplexResult = (itemResult to fluidResult).toComplex()

        override fun getId(): ResourceLocation = build().map(HTItemResult::getId, HTFluidResult::getId)
    }
}
