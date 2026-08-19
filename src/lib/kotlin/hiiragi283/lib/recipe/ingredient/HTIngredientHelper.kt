package hiiragi283.lib.recipe.ingredient

import hiiragi283.lib.registry.isAir
import hiiragi283.lib.registry.isEmpty
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

/**
 * [TypedInstance]の変換を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTIngredientHelper {
    //    Fluid    //

    /**
     * [FluidInstance]を[FluidStack]に変換します。
     */
    @JvmStatic
    fun unwrap(instance: FluidInstance): FluidStack = when (instance) {
        is FluidStack -> instance
        is FluidStackTemplate -> instance.create()
        else -> FluidStack(instance.typeHolder(), instance.amount())
    }

    /**
     * [FluidInstance]が空かどうか判定します。
     */
    @JvmStatic
    fun isEmpty(instance: FluidInstance): Boolean = when (instance) {
        is FluidStack -> instance.isEmpty
        is FluidStackTemplate -> false
        else -> instance.typeHolder().isEmpty || instance.amount() <= 0
    }

    //    Item    //

    /**
     * [ItemInstance]を[ItemStack]に変換します。
     */
    @JvmStatic
    fun unwrap(instance: ItemInstance): ItemStack = when (instance) {
        is ItemStack -> instance
        is ItemStackTemplate -> instance.create()
        else -> ItemStack(instance.typeHolder(), instance.count())
    }

    /**
     * [ItemInstance]が空かどうか判定します。
     */
    @JvmName("isEmptyItem")
    @JvmStatic
    fun isEmpty(instance: ItemInstance): Boolean = when (instance) {
        is ItemStack -> instance.isEmpty
        is ItemStackTemplate -> false
        else -> instance.typeHolder().isAir || instance.count() <= 0
    }
}
