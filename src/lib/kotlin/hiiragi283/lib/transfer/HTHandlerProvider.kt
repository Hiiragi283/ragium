package hiiragi283.lib.transfer

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.item.ItemResourceHandler
import net.minecraft.core.Direction

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTHandlerProvider {
    /**
     * 指定した[面][direction]から[FluidResourceHandler]を取得します。
     * @return 取得できなかった場合は`null`
     */
    fun getFluidHandler(direction: Direction?): FluidResourceHandler?

    /**
     * 指定した[面][direction]から[ItemResourceHandler]を取得します。
     * @return 取得できなかった場合は`null`
     */
    fun getItemHandler(direction: Direction?): ItemResourceHandler?
}
