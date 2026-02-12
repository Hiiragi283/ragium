package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

class HTSingleRecipeBuilder<ING : Any, RES : HTIdLike>(prefix: String, private val factory: Factory<ING, RES, *>) :
    HTProcessingRecipeBuilder(prefix) {
    lateinit var ingredient: ING
    lateinit var result: RES

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTProcessingRecipe<*> = factory.create(ingredient, result, subParameters())

    //    Factory    //

    fun interface Factory<ING : Any, RES : HTIdLike, RECIPE : HTProcessingRecipe<*>> {
        fun create(ingredient: ING, result: RES, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
