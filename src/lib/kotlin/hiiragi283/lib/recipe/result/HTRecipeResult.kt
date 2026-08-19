package hiiragi283.lib.recipe.result

import hiiragi283.lib.resource.HTIdLike

interface HTRecipeResult<STACK : Any> : HTIdLike {
    fun create(): STACK
}
