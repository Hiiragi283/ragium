package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import java.util.function.ToIntFunction

object HTIngredientHelper {
    //    Item    //

    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: ToIntFunction<ItemStack>, action: HTStorageAction): Int {
        val stackIn: ItemStack = slot.getStack()
        if (stackIn.hasCraftingRemainingItem() && stackIn.count == 1) {
            slot.setStack(stackIn.craftingRemainingItem)
            return 0
        } else {
            return slot.shrinkStack(ingredient.applyAsInt(slot.getStack()), action)
        }
    }

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return 実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: HTItemIngredient, action: HTStorageAction): Int =
        shrinkStack(slot, ingredient::getRequiredAmount, action)

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: Optional<HTItemIngredient>, action: HTStorageAction): Int =
        ingredient.map { ingredient1 -> shrinkStack(slot, ingredient1, action) }.orElse(0)

    //    Fluid    //

    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: ToIntFunction<FluidStack>, action: HTStorageAction): Int =
        tank.extract(ingredient.applyAsInt(tank.getStack()), action, HTStorageAccess.INTERNAl).amount

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: HTFluidIngredient, action: HTStorageAction): Int =
        shrinkStack(tank, ingredient::getRequiredAmount, action)

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: Optional<HTFluidIngredient>, action: HTStorageAction): Int =
        ingredient.map { ingredient1 -> shrinkStack(tank, ingredient1, action) }.orElse(0)
}
