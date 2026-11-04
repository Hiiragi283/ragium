package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import net.minecraft.world.item.crafting.Ingredient

/**
 * 触媒を受け取るレシピビルダー向けのインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemCatalystAcceptor {
    operator fun HTItemCatalyst.unaryPlus()

    operator fun Ingredient.unaryPlus() {
        +HTItemCatalyst(this)
    }

    fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        +IngredientBuilder.build(builderAction)
    }
}
