package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTSimulatingRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.data.recipe.base.HTComplexResultRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.impl.recipe.HTEntitySimulatingRecipe
import net.minecraft.core.HolderSet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import java.util.Optional

class HTSimulatingRecipeBuilder<T : Any>(
    prefix: String,
    private val factory: Factory<T, *>,
    private val ingredient: Optional<HTItemIngredient>,
    private val catalyst: T,
) : HTComplexResultRecipeBuilder<HTSimulatingRecipeBuilder<T>>(prefix) {
    companion object {
        @JvmStatic
        fun block(ingredient: HTItemIngredient?, catalyst: HolderSet<Block>): HTSimulatingRecipeBuilder<HolderSet<Block>> =
            HTSimulatingRecipeBuilder(
                RagiumConst.SIMULATING_BLOCK,
                ::HTBlockSimulatingRecipe,
                ingredient.wrapOptional(),
                catalyst,
            )

        @JvmStatic
        fun entity(ingredient: HTItemIngredient?, catalyst: HolderSet<EntityType<*>>): HTSimulatingRecipeBuilder<HolderSet<EntityType<*>>> =
            HTSimulatingRecipeBuilder(
                RagiumConst.SIMULATING_ENTITY,
                ::HTEntitySimulatingRecipe,
                ingredient.wrapOptional(),
                catalyst,
            )
    }

    override fun createRecipe(): HTSimulatingRecipe = factory.create(ingredient, catalyst, toIorResult())

    fun interface Factory<T : Any, RECIPE : HTSimulatingRecipe> {
        fun create(ingredient: Optional<HTItemIngredient>, catalyst: T, results: Ior<HTItemResult, HTFluidResult>): RECIPE
    }
}
