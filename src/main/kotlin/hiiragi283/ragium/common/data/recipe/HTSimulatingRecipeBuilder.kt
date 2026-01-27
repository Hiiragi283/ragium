package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import net.minecraft.core.HolderSet
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import org.apache.commons.lang3.math.Fraction
import java.util.*

class HTSimulatingRecipeBuilder<T : Any>(prefix: String, private val factory: Factory<T, *>) : HTAbstractComplexRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun block(output: RecipeOutput, builderAction: HTSimulatingRecipeBuilder<HolderSet<Block>>.() -> Unit) {
            HTSimulatingRecipeBuilder(RagiumConst.SIMULATING_BLOCK, ::HTBlockSimulatingRecipe)
                .apply(builderAction)
                .save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun entity(output: RecipeOutput, builderAction: HTSimulatingRecipeBuilder<HolderSet<EntityType<*>>>.() -> Unit) {
            HTSimulatingRecipeBuilder(RagiumConst.SIMULATING_ENTITY, ::HTEntitySimulatingRecipe)
                .apply(builderAction)
                .save(output)
        }
    }

    var ingredient: HTItemIngredient? = null
    lateinit var catalyst: T

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSimulatingRecipe<*> =
        factory.create(Optional.ofNullable(ingredient), catalyst, result.build(), time, exp)

    //    Factory    //

    fun interface Factory<T : Any, RECIPE : HTSimulatingRecipe<*>> {
        fun create(
            ingredient: Optional<HTItemIngredient>,
            catalyst: T,
            results: HTComplexResult,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
