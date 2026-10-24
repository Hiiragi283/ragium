package hiiragi283.ragium.common.gui.slot

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [TransactionContext]に基づいて中身の出し入れを行うスロット向けのインターフェースです。
 *
 * 参照 : [Mekanism - ITransactionalSlot](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/slot/ITransactionalSlot.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTTransactionSlot {
    fun insert(resource: ItemResource, amount: Int, transaction: TransactionContext): Int

    fun extract(player: Player, resource: ItemResource, amount: Int, transaction: TransactionContext): Int

    fun canMergeWith(stack: ItemStack): Boolean = true
}
