package hiiragi283.lib.recipe.result

import hiiragi283.lib.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Objects

/**
 * アイテムと液体の完成品を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemAndFluidResult(val item: ItemStack, val fluid: FluidStack) {
    companion object {
        /**
         * @since 26.1.7
         */
        @JvmStatic
        fun from(result: HTItemResult?): HTItemAndFluidResult =
            HTItemAndFluidResult(result.createOrEmpty(), FluidStack.EMPTY)

        /**
         * @since 26.1.7
         */
        @JvmStatic
        fun from(result: HTFluidResult?): HTItemAndFluidResult =
            HTItemAndFluidResult(ItemStack.EMPTY, result.createOrEmpty())

        /**
         * @since 26.1.7
         */
        @JvmStatic
        fun from(item: HTItemResult?, fluid: HTFluidResult?): HTItemAndFluidResult =
            HTItemAndFluidResult(item.createOrEmpty(), fluid.createOrEmpty())

        /**
         * @since 26.1.7
         */
        @JvmStatic
        fun from(results: Ior<HTItemResult, HTFluidResult>): HTItemAndFluidResult {
            val (item: HTItemResult?, fluid: HTFluidResult?) = results.toPair()
            return from(item, fluid)
        }
    }

    override fun equals(other: Any?): Boolean = (other as? HTItemAndFluidResult)?.let {
        ItemStack.isSameItemSameComponents(it.item, this.item) && FluidStack.isSameFluid(it.fluid, this.fluid)
    } ?: false

    override fun hashCode(): Int =
        Objects.hash(ItemStack.hashItemAndComponents(item), FluidStack.hashFluidAndComponents(fluid))
}
