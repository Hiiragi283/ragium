package hiiragi283.lib.recipe.result

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Objects

/**
 * アイテムと液体の完成品をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemAndFluidStack(val item: ItemStack, val fluid: FluidStack) {
    override fun equals(other: Any?): Boolean = (other as? HTItemAndFluidStack)?.let {
        ItemStack.isSameItemSameComponents(it.item, this.item) && FluidStack.isSameFluid(it.fluid, this.fluid)
    } ?: false

    override fun hashCode(): Int =
        Objects.hash(ItemStack.hashItemAndComponents(item), FluidStack.hashFluidAndComponents(fluid))
}
