package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTItemCatalystAcceptor
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidIngredientAcceptor
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.result.HTItemOrFluidResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTItemReactingRecipe
import net.minecraft.resources.Identifier
import java.util.Optional

class RTItemReactingRecipeBuilder :
    HTProgressRecipeBuilder<RTItemReactingRecipe>(RagiumConstants.REACTING),
    HTItemOrFluidIngredientAcceptor,
    HTItemCatalystAcceptor,
    HTItemOrFluidResultBuilder.Holder {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTItemReactingRecipe = RTItemReactingRecipe(ingredient, catalyst, result, progressData)

    // Ingredients
    private var itemIngredient: Optional<HTItemIngredient> by HTDelegates.optionalInitialize()
    private var fluidIngredient: Optional<HTFluidIngredient> by HTDelegates.optionalInitialize()
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient> by lazy { Ior.fromNullable(itemIngredient, fluidIngredient).orElseThrow() }

    override operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = Optional.of(this)
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = Optional.of(this)
    }

    // Catalyst
    @PublishedApi internal var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    override operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }

    // Results
    override val builder: HTItemOrFluidResultBuilder = HTItemOrFluidResultBuilder()
}
