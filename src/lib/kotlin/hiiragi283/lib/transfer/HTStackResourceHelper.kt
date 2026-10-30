package hiiragi283.lib.transfer

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * スタックとリソースを相互に変換するヘルパークラスです。
 * @param S リソースと量を束ねたクラス
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTStackResourceHelper<S : Any, T : Resource> {
    /**
     * スタックをリソースに変換します。
     */
    abstract fun getResourceFrom(stack: S): T

    /**
     * スタックから数量を取得します。
     */
    abstract fun getAmountFrom(stack: S): Int

    /**
     * 指定したスタックとリソースが等しいかどうか判定します。
     */
    abstract fun isSame(stack: S, resource: T): Boolean

    /**
     * リソースと数量からスタックを作成します。
     */
    abstract fun createStack(resource: T, amount: Int): S

    //    Fluid    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    data object Fluid : HTStackResourceHelper<FluidStack, FluidResource>() {
        override fun getResourceFrom(stack: FluidStack): FluidResource = FluidResource.of(stack)

        override fun getAmountFrom(stack: FluidStack): Int = stack.amount()

        override fun isSame(stack: FluidStack, resource: FluidResource): Boolean = resource.matches(stack)

        override fun createStack(resource: FluidResource, amount: Int): FluidStack = resource.toStack(amount)
    }

    //    Item    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    data object Item : HTStackResourceHelper<ItemStack, ItemResource>() {
        override fun getResourceFrom(stack: ItemStack): ItemResource = ItemResource.of(stack)

        override fun getAmountFrom(stack: ItemStack): Int = stack.count()

        override fun isSame(stack: ItemStack, resource: ItemResource): Boolean = resource.matches(stack)

        override fun createStack(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)
    }
}
