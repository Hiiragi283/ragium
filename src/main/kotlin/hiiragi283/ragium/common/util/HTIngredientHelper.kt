package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import java.util.Optional

object HTIngredientHelper {
    //    Item    //

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot, ingredient: HTItemIngredient, simulate: Boolean): Int =
        slot.shrinkStack(ingredient.getRequiredAmount(slot.getStack()), simulate)

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot, ingredient: Optional<HTItemIngredient>, simulate: Boolean): Int =
        ingredient.map { ingredient1 -> shrinkStack(slot, ingredient1, simulate) }.orElse(0)

    //    Fluid    //

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: HTFluidIngredient, simulate: Boolean): Int =
        tank.shrinkStack(ingredient.getRequiredAmount(tank.getStack()), simulate)

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: Optional<HTFluidIngredient>, simulate: Boolean): Int =
        ingredient.map { ingredient1 -> shrinkStack(tank, ingredient1, simulate) }.orElse(0)
}
