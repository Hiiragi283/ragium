package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension
import net.neoforged.neoforge.energy.IEnergyStorage

object HTEnergyCapabilities : HTMultiCapability<IEnergyStorage, IEnergyStorage> {
    override val block: BlockCapability<IEnergyStorage, Direction?> = Capabilities.EnergyStorage.BLOCK
    override val entity: EntityCapability<IEnergyStorage, Direction?> = Capabilities.EnergyStorage.ENTITY
    override val item: ItemCapability<IEnergyStorage, Void?> = Capabilities.EnergyStorage.ITEM

    fun getStorage(level: ILevelExtension, pos: BlockPos, side: Direction?): HTEnergyStorage? =
        this.getCapability(level, pos, side)?.let(::wrapStorage)

    fun getStorage(entity: Entity, side: Direction?): HTEnergyStorage? = this.getCapability(entity, side)?.let(::wrapStorage)

    fun getStorage(stack: IItemStackExtension): HTEnergyStorage? = this.getCapability(stack)?.let(::wrapStorage)

    fun getStorage(stack: ImmutableItemStack): HTEnergyStorage? = this.getStorage(stack.stack)

    private fun wrapStorage(storage: IEnergyStorage): HTEnergyStorage? = when (storage) {
        is HTEnergyStorage -> storage
        else -> object : HTEnergyStorage, HTContentListener.Empty, HTValueSerializable.Empty {
            override fun getAmount(): Int = storage.energyStored

            override fun getCapacity(): Int = storage.maxEnergyStored

            override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.receiveEnergy(amount, action.simulate)

            override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.extractEnergy(amount, action.simulate)
        }
    }
}
