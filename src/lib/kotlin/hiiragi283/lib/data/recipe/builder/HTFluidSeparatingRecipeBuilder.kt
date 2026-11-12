@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.collection.toNelOrNull
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class HTFluidSeparatingRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder.SingleResult<RECIPE>(prefix),
    HTItemOrFluidResultAcceptor {
    override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTFluidIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Results
    @PublishedApi internal var itemResult: HTItemResult? by HTDelegates.onceOrNull()

    @PublishedApi internal var fluidResults: MutableList<HTFluidResult> = ObjectArrayList()

    override val result: HTFluidSeparatingRecipe.Result by lazy {
        Ior.fromNullable(fluidResults.toNelOrNull(), itemResult)
            ?.let(HTFluidSeparatingRecipe::Result)
            ?: error("Either item or fluid result required")
    }

    override operator fun HTItemResult.unaryPlus() {
        itemResult = this
    }

    override operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
    }

    //    Factory    //

    /**
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: HTFluidIngredient, result: HTFluidSeparatingRecipe.Result, progressData: HTProgressData): RECIPE
    }
}
