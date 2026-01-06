package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

class HTSimulatingRecipeBuilder<T : Any>(
    prefix: String,
    private val factory: Factory<T, *>,
    private val ingredient: Optional<HTItemIngredient>,
    private val catalyst: T,
) : HTAbstractComplexRecipeBuilder<HTSimulatingRecipeBuilder<T>>(prefix) {
    companion object {
        @JvmStatic
        fun block(ingredient: HTItemIngredient?, catalyst: HolderSet<Block>): HTSimulatingRecipeBuilder<HolderSet<Block>> =
            HTSimulatingRecipeBuilder(
                RagiumConst.SIMULATING_BLOCK,
                ::HTBlockSimulatingRecipe,
                Optional.ofNullable(ingredient),
                catalyst,
            )

        @JvmStatic
        fun entity(ingredient: HTItemIngredient?, catalyst: HolderSet<EntityType<*>>): HTSimulatingRecipeBuilder<HolderSet<EntityType<*>>> =
            HTSimulatingRecipeBuilder(
                RagiumConst.SIMULATING_ENTITY,
                ::HTEntitySimulatingRecipe,
                Optional.ofNullable(ingredient),
                catalyst,
            )
    }

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTSimulatingRecipe<*> = factory.create(ingredient, catalyst, toIorResult(), time, exp)

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
