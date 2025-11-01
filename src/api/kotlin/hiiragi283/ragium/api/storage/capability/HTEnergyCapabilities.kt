package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
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

object HTEnergyCapabilities : HTMultiCapability.Simple<IEnergyStorage> {
    override val block: BlockCapability<IEnergyStorage, Direction?> = Capabilities.EnergyStorage.BLOCK
    override val entity: EntityCapability<IEnergyStorage, Direction?> = Capabilities.EnergyStorage.ENTITY
    override val item: ItemCapability<IEnergyStorage, Void?> = Capabilities.EnergyStorage.ITEM

    fun getBattery(level: ILevelExtension, pos: BlockPos, side: Direction?): HTEnergyBattery? =
        this.getCapability(level, pos, side)?.let { wrapStorage(it, side) }

    fun getBattery(entity: Entity, side: Direction?): HTEnergyBattery? = this.getCapability(entity, side)?.let { wrapStorage(it, side) }

    fun getBattery(stack: IItemStackExtension): HTEnergyBattery? = this.getCapability(stack)?.let { wrapStorage(it, null) }

    fun getBattery(stack: ImmutableItemStack?): HTEnergyBattery? = this.getCapability(stack)?.let { wrapStorage(it, null) }

    private fun wrapStorage(storage: IEnergyStorage, context: Direction?): HTEnergyBattery? = when (storage) {
        is HTEnergyHandler -> storage.getEnergyBattery(context)
        else -> object : HTEnergyBattery, HTContentListener.Empty, HTValueSerializable.Empty {
            override fun getAmount(): Int = storage.energyStored

            override fun getCapacity(): Int = storage.maxEnergyStored

            override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.receiveEnergy(amount, action.simulate)

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.extractEnergy(amount, action.simulate)
        }
    }
}
