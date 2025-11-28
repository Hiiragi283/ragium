package hiiragi283.ragium.impl.data.recipe.base

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder

abstract class HTGeneratorRecipeBuilder<BUILDER : HTGeneratorRecipeBuilder<BUILDER>>(prefix: String, protected val duration: Int) :
    HTRecipeBuilder<BUILDER>(prefix)
