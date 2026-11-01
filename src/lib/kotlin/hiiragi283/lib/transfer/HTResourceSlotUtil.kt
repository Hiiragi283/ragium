package hiiragi283.lib.transfer

import hiiragi283.lib.util.fixedFraction
import net.minecraft.CrashReport
import net.minecraft.ReportedException
import net.minecraft.util.Mth
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 * @see net.neoforged.neoforge.transfer.ResourceHandlerUtil
 */
data object HTResourceSlotUtil {
    /**
     * 参照 : [NeoForge - ResourceHandlerUtil.getRedstoneSignalFromResourceHandler][net.neoforged.neoforge.transfer.ResourceHandlerUtil.getRedstoneSignalFromResourceHandler]
     */
    @JvmStatic
    fun calculateRedstoneLevel(views: Iterable<HTResourceView<*>>): Int {
        var proportion = 0.0f
        var sampleCount = 0 // Number of samples in proportion
        for (view: HTResourceView<*> in views) {
            val indexFill: Int = view.amount
            if (indexFill > 0) {
                val capacity: Int = view.getCapacity(view.resource)
                if (capacity > 0) {
                    proportion += fixedFraction(indexFill, capacity)
                    sampleCount++
                }
            }
        }
        if (sampleCount == 0) return Redstone.SIGNAL_NONE
        proportion /= sampleCount.toFloat()
        return Mth.lerpDiscrete(proportion, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)
    }

    @JvmStatic
    fun calculateRedstoneLevel(view: HTResourceView<*>): Int = Mth.lerpDiscrete(view.currentFilledLevel, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    /**
     * @return 移動したリソースの量
     * @see net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil.move
     */
    @JvmStatic
    fun <T : Resource> move(slotFrom: HTResourceSlot<T>?, slotTo: HTResourceSlot<T>?, amount: Int, parent: TransactionContext?): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (slotFrom == null || slotTo == null || amount == 0) return 0
        return runCatching {
            // 移動元のリソースが空の場合，何も移動しない
            val fromResource: T = slotFrom.resource
            if (fromResource.isEmpty) return@runCatching 0
            useTransaction(parent) { transaction: Transaction ->
                // 最大でどれだけ搬出可能か取得する
                val maxExtracted: Int
                useTransaction(transaction) { transaction1: Transaction ->
                    maxExtracted = slotFrom.extract(fromResource, amount, transaction1, HTTransferAccess.INTERNAL)
                }
                // 搬出できる量がない場合は，実際に移動しない
                if (maxExtracted == 0) return@runCatching 0
                // 搬入できる量を取得する
                val inserted: Int = slotTo.insert(fromResource, maxExtracted, transaction, HTTransferAccess.INTERNAL)
                // 搬入できる量と搬出できる量が異なる場合は移動しない
                if (inserted != slotFrom.extract(fromResource, inserted, transaction, HTTransferAccess.INTERNAL)) {
                    return@runCatching 0
                }
                transaction.commit()
                inserted
            }
        }.getOrElse {
            val report: CrashReport = CrashReport.forThrowable(it, "Moving resources between slots")
            report.addCategory("Move details")
                .setDetail("Input", slotFrom::toString)
                .setDetail("Output", slotTo::toString)
                .setDetail("Amount", amount)
                .setDetail("Transaction", parent.toString())
            throw ReportedException(report)
        }
    }
}
