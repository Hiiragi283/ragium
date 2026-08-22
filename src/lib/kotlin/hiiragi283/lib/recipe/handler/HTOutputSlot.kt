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
     * 完成品を搬入します。
     * @param stack 搬入する完成品
     * @param transaction 現在のトランザクション
     * @return 実際に搬入される数量
     */
    fun insert(stack: STACK, transaction: TransactionContext): Int

    /**
     * 完成品を搬入できるか判定します。
     * @param stack 搬入する完成品
     * @param transaction 現在のトランザクション
     * @return 実際に消費される数量が[stack]の数量と等しい場合は`true`
     */
    fun canInsert(stack: STACK, transaction: TransactionContext): Boolean

    //    Single    //

    /**
     * [アイテム][ItemStack]向けの[HTOutputSlot]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleItem(private val slot: HTItemSlot) : HTOutputSlot<ItemStack> {
        override fun insert(stack: ItemStack, transaction: TransactionContext): Int {
            if (stack.isEmpty) return 0
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            return slot.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)
        }

        override fun canInsert(stack: ItemStack, transaction: TransactionContext): Boolean = insert(stack, transaction) == stack.count()
    }

    /**
     * [液体][FluidStack]向けの[HTOutputSlot]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class SingleFluid(private val tank: HTFluidTank) : HTOutputSlot<FluidStack> {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Int {
            if (stack.isEmpty) return 0
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            return tank.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)
        }

        override fun canInsert(stack: FluidStack, transaction: TransactionContext): Boolean = insert(stack, transaction) == stack.amount()
    }
}
