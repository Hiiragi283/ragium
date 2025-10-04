package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import java.util.function.ToIntFunction

object HTIngredientHelper {
    //    Item    //

    @JvmStatic
    fun shrinkStack(slot: HTItemSlot, ingredient: ToIntFunction<ItemStack>, simulate: Boolean): Int {
        val stackIn: ItemStack = slot.getStack()
        if (stackIn.hasCraftingRemainingItem() && stackIn.count == 1) {
            slot.setStack(stackIn.craftingRemainingItem)
            return 0
        } else {
            return slot.shrinkStack(ingredient.applyAsInt(slot.getStack()), simulate)
        }
    }

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot, ingredient: HTItemIngredient, simulate: Boolean): Int =
        shrinkStack(slot, ingredient::getRequiredAmount, simulate)

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

    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: ToIntFunction<FluidStack>, simulate: Boolean): Int =
        tank.extract(ingredient.applyAsInt(tank.getStack()), simulate, HTStorageAccess.INTERNAl).amount

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: HTFluidIngredient, simulate: Boolean): Int =
        shrinkStack(tank, ingredient::getRequiredAmount, simulate)

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
