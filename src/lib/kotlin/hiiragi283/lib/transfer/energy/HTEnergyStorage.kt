package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * 外部との搬入/搬出が可能な[HTEnergyView]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IEnergyContainer](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/energy/IEnergyContainer.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTEnergyStorage :
    HTEnergyView,
    ValueIOSerializable {
    fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int
}
