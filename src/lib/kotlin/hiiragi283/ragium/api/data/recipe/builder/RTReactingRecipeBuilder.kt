package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNelOrNull
import hiiragi283.lib.data.recipe.builder.HTItemCatalystAcceptor
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidIngredientAcceptor
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidResultAcceptor
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier

class RTReactingRecipeBuilder :
    HTProgressRecipeBuilder<RTReactingRecipe>(RagiumConstants.REACTING),
    HTItemOrFluidIngredientAcceptor,
    HTItemCatalystAcceptor,
    HTItemOrFluidResultAcceptor {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTReactingRecipe = RTReactingRecipe(
        Ior.fromNullable(itemIngredients.toNelOrNull(), fluidIngredients.toNelOrNull())
            ?.let(RTReactingRecipe::Ingredients)
            ?: error("Either item or fluid ingredient required"),
        catalyst,
        result,
        progressData,
    )

    // Ingredients
    private val itemIngredients: MutableList<HTItemIngredient> = ObjectArrayList()
    private val fluidIngredients: MutableList<HTFluidIngredient> = ObjectArrayList()

    override operator fun HTItemIngredient.unaryPlus() {
        itemIngredients += this
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredients += this
    }

    // Catalyst
    @PublishedApi internal var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    override operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }

    // Results
    private val itemResults: MutableList<HTItemResult> = ObjectArrayList()
    private val fluidResults: MutableList<HTFluidResult> = ObjectArrayList()

    val result: RTReactingRecipe.Result by lazy {
        Ior.fromNullable(itemResults.toNelOrNull(), fluidResults.toNelOrNull())
            ?.let(RTReactingRecipe::Result)
            ?: error("Either item or fluid result required")
    }

    override operator fun HTItemResult.unaryPlus() {
        itemResults += this
    }

    override operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
    }
}
