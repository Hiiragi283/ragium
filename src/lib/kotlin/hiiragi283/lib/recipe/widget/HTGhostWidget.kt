package hiiragi283.lib.recipe.widget

import java.util.function.Consumer
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * レシピビューワーからオブジェクトをドラッグ&ドロップ可能なウィジェットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IRecipeViewerGhostTarget](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/recipe_viewer/interfaces/IRecipeViewerGhostTarget.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTGhostWidget {
    /**
     * 保持している[GhostIngredientConsumer]を取得します。
     */
    fun getGhostConsumer(): GhostIngredientConsumer?

    /**
     * ドラッグ&ドロップの処理を担うインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface GhostIngredientConsumer : Consumer<Any> {
        /**
         * 指定した[ingredient]から，対応するオブジェクトに変換します。
         * @return 対応していない場合は`null`
         */
        fun supportedTarget(ingredient: Any): Any?
    }

    /**
     * [ItemStack]向けの[GhostIngredientConsumer]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    fun interface ItemConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): ItemStack? = when (ingredient) {
            is ItemStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }

    /**
     * [FluidStack]向けの[GhostIngredientConsumer]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    fun interface FluidConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): FluidStack? = when (ingredient) {
            is FluidStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }
}
