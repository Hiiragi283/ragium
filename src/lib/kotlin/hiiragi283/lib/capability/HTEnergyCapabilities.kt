package hiiragi283.lib.capability

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [EnergyHandler]向けの[HTMultiCapability]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTEnergyCapabilities : HTMultiCapability<EnergyHandler> {
    override val block: BlockCapability<EnergyHandler, Direction?> = Capabilities.Energy.BLOCK
    override val entity: EntityCapability<EnergyHandler, Direction?> = Capabilities.Energy.ENTITY
    override val item: ItemCapability<EnergyHandler, ItemAccess> = Capabilities.Energy.ITEM

    /**
     * 指定された[handler]を[HTEnergyStorage]に展開します。
     */
    fun unwrap(handler: EnergyHandler): HTEnergyStorage = when (handler) {
        is HTEnergyHandler -> handler.storage

        else -> object : HTEnergyStorage {
            override fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int = handler.insert(amount, transaction)

            override fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int = handler.extract(amount, transaction)

            override val amount: Int get() = handler.amountAsInt
            override val capacity: Int get() = handler.capacityAsInt
        }
    }

    //    Block    //

    /**
     * 指定した引数から[HTEnergyStorage]を返します。
     * @return [HTEnergyStorage]が見つからない場合は`null`
     */
    fun getStorage(level: Level, pos: BlockPos, side: Direction?): HTEnergyStorage? = getCapability(level, pos, side)?.let(::unwrap)

    //    Entity    //

    /**
     * 指定した引数から[HTEnergyStorage]を返します。
     * @return [HTEnergyStorage]が見つからない場合は`null`
     */
    fun getStorage(entity: Entity, side: Direction?): HTEnergyStorage? = getCapability(entity, side)?.let(::unwrap)

    //    Item    //

    /**
     * 指定した引数から[HTEnergyStorage]を返します。
     * @return [HTEnergyStorage]が見つからない場合は`null`
     */
    fun getStorage(access: ItemAccess): HTEnergyStorage? = getCapability(access)?.let(::unwrap)

    /**
     * 指定した引数から[HTEnergyStorage]を返します。
     * @return [HTEnergyStorage]が見つからない場合は`null`
     */
    fun getStorage(stack: ItemStack): HTEnergyStorage? = getCapability(stack)?.let(::unwrap)
}
