package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの入力となるスロットを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTInputSlot {
    /**
     * 材料を消費します。
     * @param amount 搬出する数量
     * @param transaction 現在のトランザクション
     * @return 実際に消費される数量
     */
    fun extract(amount: Int, transaction: TransactionContext): Int

    /**
     * 材料を消費できるか判定します。
     * @param amount 搬出する数量
     * @param transaction 現在のトランザクション
     * @return 実際に消費される数量が[amount]と等しい場合は`true`
     */
    fun canExtract(amount: Int, transaction: TransactionContext): Boolean = extract(amount, transaction) == amount

    //    Single    //

    /**
     * 単一のスロットに対する[HTInputSlot]の拡張インターフェースです。
     * @param STACK 保持しているスタックのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface Single<STACK : Any> : HTInputSlot {
        /**
         * 保持しているスタックを取得します。
         */
        fun getStack(): STACK
    }

    /**
     * [アイテム][ItemStack]向けの[Single]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleItem(private val slot: HTItemSlot) : Single<ItemStack> {
        override fun getStack(): ItemStack = slot.getItemStack()

        override fun extract(amount: Int, transaction: TransactionContext): Int =
            slot.extractSelf(amount, transaction, HTTransferAccess.INTERNAL)
    }

    /**
     * [液体][FluidStack]向けの[Single]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleFluid(private val tank: HTFluidTank) : Single<FluidStack> {
        override fun getStack(): FluidStack = tank.getFluidStack()

        override fun extract(amount: Int, transaction: TransactionContext): Int =
            tank.extractSelf(amount, transaction, HTTransferAccess.INTERNAL)
    }
}
