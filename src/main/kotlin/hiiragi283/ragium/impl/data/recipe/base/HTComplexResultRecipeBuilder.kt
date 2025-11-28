package hiiragi283.ragium.impl.data.recipe.base

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.toComplex
import net.minecraft.resources.ResourceLocation

abstract class HTComplexResultRecipeBuilder<BUILDER : HTComplexResultRecipeBuilder<BUILDER>>(prefix: String) :
    HTRecipeBuilder<BUILDER>(prefix) {
    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun setResult(result: HTItemResult?): BUILDER {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
        return self()
    }

    fun setResult(result: HTFluidResult?): BUILDER {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
        return self()
    }

    final override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::id, HTFluidResult::id)

    protected fun toIorResult(): HTComplexResult = (itemResult to fluidResult).toComplex()
}
