package hiiragi283.ragium.api.storage.capability

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

val IItemHandler.slotRange: IntRange get() = (0..<this.slots)

val IFluidHandler.tankRange: IntRange get() = (0..<this.tanks)

//    HTMultiCapability    //

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getBattery(
    level: ILevelExtension,
    pos: BlockPos,
    side: Direction?,
): HTEnergyBattery? = this.getCapability(level, pos, side)?.let { wrapBattery(it, side) }

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getBattery(stack: IItemStackExtension): HTEnergyBattery? =
    this.getCapability(stack)?.let {
        wrapBattery(it, null)
    }

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getBattery(stack: ImmutableItemStack): HTEnergyBattery? = this.getBattery(stack.stack)

private fun wrapBattery(storage: IEnergyStorage, side: Direction?): HTEnergyBattery? = when (storage) {
    is HTEnergyHandler -> storage.getEnergyBattery(side)
    else -> object : HTEnergyBattery, HTContentListener.Empty, HTValueSerializable.Empty {
        override fun getAmountAsLong(): Long = storage.energyStored.toLong()

        override fun getCapacityAsLong(): Long = storage.maxEnergyStored.toLong()

        override fun insertEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
            storage.receiveEnergy(Ints.saturatedCast(amount), action.simulate).toLong()

        override fun extractEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
            storage.extractEnergy(Ints.saturatedCast(amount), action.simulate).toLong()
    }
}
