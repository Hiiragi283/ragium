package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemOrFluidIngredientAcceptor {
    operator fun HTItemIngredient.unaryPlus()

    operator fun HTFluidIngredient.unaryPlus()

    fun itemIngredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        +HTItemIngredientBuilder.build(builderAction)
    }

    fun fluidIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        +HTFluidIngredientBuilder.build(builderAction)
    }
}
