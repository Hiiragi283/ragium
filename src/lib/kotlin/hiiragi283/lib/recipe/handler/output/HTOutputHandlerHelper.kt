package hiiragi283.lib.recipe.handler.output

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
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object HTOutputHandlerHelper {
    //    Fluid    //

    /**
     * 単一の[HTFluidTank]を[液体][FluidStack]向けの[HTOutputHandler]に変換します。
     * @since 26.1.0
     */
    @JvmStatic
    fun forFluid(tank: HTFluidTank): HTOutputHandler<FluidStack> = SingleFluid(tank)

    @JvmRecord
    private data class SingleFluid(private val tank: HTFluidTank) : HTOutputHandler<FluidStack> {
        override fun take(stack: FluidStack, transaction: TransactionContext): HTOutputHandler.TakeResult {
            if (stack.isEmpty) return HTOutputHandler.TakeResult.NONE
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            return when (tank.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)) {
                amount -> HTOutputHandler.TakeResult.FULL
                else -> HTOutputHandler.TakeResult.PARTIALLY
            }
        }
    }

    /**
     * 複数の[HTFluidTank]を[アイテム][FluidStack]向けの[HTOutputHandler]に変換します。
     * @since 26.1.13
     */
    @JvmStatic
    fun forFluids(tanks: List<HTFluidTank>): HTOutputHandler<FluidStack> = MultiFluids(tanks)

    @JvmRecord
    private data class MultiFluids(private val tanks: List<HTFluidTank>) : HTOutputHandler<FluidStack> {
        override fun take(stack: FluidStack, transaction: TransactionContext): HTOutputHandler.TakeResult {
            if (stack.isEmpty) return HTOutputHandler.TakeResult.NONE
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            var inserted = 0
            for (tank: HTFluidTank in tanks) {
                inserted += tank.insert(resource, amount - inserted, transaction, HTTransferAccess.INTERNAL)
                if (inserted == amount) break
            }
            return when (inserted) {
                amount -> HTOutputHandler.TakeResult.FULL
                else -> HTOutputHandler.TakeResult.PARTIALLY
            }
        }
    }

    //    Item    //

    /**
     * 単一の[HTItemSlot]を[アイテム][ItemStack]向けの[HTOutputHandler]に変換します。
     * @since 26.1.0
     */
    @JvmStatic
    fun forItem(slot: HTItemSlot): HTOutputHandler<ItemStack> = SingleItem(slot)

    @JvmRecord
    private data class SingleItem(private val slot: HTItemSlot) : HTOutputHandler<ItemStack> {
        override fun take(stack: ItemStack, transaction: TransactionContext): HTOutputHandler.TakeResult {
            if (stack.isEmpty) return HTOutputHandler.TakeResult.NONE
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            return when (slot.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)) {
                amount -> HTOutputHandler.TakeResult.FULL
                else -> HTOutputHandler.TakeResult.PARTIALLY
            }
        }
    }

    /**
     * 複数の[HTItemSlot]を[アイテム][ItemStack]向けの[HTOutputHandler]に変換します。
     * @since 26.1.7
     */
    @JvmStatic
    fun forItems(slots: List<HTItemSlot>): HTOutputHandler<ItemStack> = MultiItems(slots)

    @JvmRecord
    private data class MultiItems(private val slots: List<HTItemSlot>) : HTOutputHandler<ItemStack> {
        override fun take(stack: ItemStack, transaction: TransactionContext): HTOutputHandler.TakeResult {
            if (stack.isEmpty) return HTOutputHandler.TakeResult.NONE
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            var inserted = 0
            for (slot: HTItemSlot in slots) {
                inserted += slot.insert(resource, amount - inserted, transaction, HTTransferAccess.INTERNAL)
                if (inserted == amount) break
            }
            return when (inserted) {
                amount -> HTOutputHandler.TakeResult.FULL
                else -> HTOutputHandler.TakeResult.PARTIALLY
            }
        }
    }
}
