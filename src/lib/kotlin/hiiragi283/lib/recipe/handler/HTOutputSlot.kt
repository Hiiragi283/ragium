package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力となるスロットを表すクラスです。
 * @param STACK 搬入するスタックのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTOutputSlot<STACK : Any> {
    /**
     * 出力を受け取ります。
     * @param stack 受け取る出力
     * @param transaction 現在のトランザクション
     */
    fun take(stack: STACK, transaction: TransactionContext): TakeResult

    //    TakeResult    //

    /**
     * [HTOutputSlot.take]の結果を表すクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    enum class TakeResult {
        /**
         * 出力を受け取れない場合
         */
        NONE,

        /**
         * 出力を一部だけ受け取れる場合
         */
        PARTIALLY,

        /**
         * 出力をすべて受け取れる場合
         */
        FULL;

        /**
         * 出力の受け取りに失敗した場合は`true`
         */
        val noneTaken: Boolean get() = this == NONE

        /**
         * 出力の受け取りに成功した場合は`true`
         */
        val anyTaken: Boolean get() = !noneTaken
    }

    //    Single    //

    /**
     * [アイテム][ItemStack]向けの[HTOutputSlot]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleItem(private val slot: HTItemSlot) : HTOutputSlot<ItemStack> {
        override fun take(stack: ItemStack, transaction: TransactionContext): TakeResult {
            if (stack.isEmpty) return TakeResult.NONE
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            return when (slot.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)) {
                0 -> TakeResult.NONE
                amount -> TakeResult.FULL
                else -> TakeResult.PARTIALLY
            }
        }
    }

    /**
     * [液体][FluidStack]向けの[HTOutputSlot]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleFluid(private val tank: HTFluidTank) : HTOutputSlot<FluidStack> {
        override fun take(stack: FluidStack, transaction: TransactionContext): TakeResult {
            if (stack.isEmpty) return TakeResult.NONE
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            return when (tank.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)) {
                0 -> TakeResult.NONE
                amount -> TakeResult.FULL
                else -> TakeResult.PARTIALLY
            }
        }
    }
}
