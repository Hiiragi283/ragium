package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
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

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getStorage(
    level: ILevelExtension,
    pos: BlockPos,
    side: Direction?,
): HTEnergyStorage? = this.getCapability(level, pos, side)?.let(::wrapStorage)

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getStorage(stack: IItemStackExtension): HTEnergyStorage? =
    this.getCapability(stack)?.let(::wrapStorage)

fun HTMultiCapability<IEnergyStorage, IEnergyStorage>.getStorage(stack: ImmutableItemStack): HTEnergyStorage? = this.getStorage(stack.stack)

fun wrapStorage(storage: IEnergyStorage): HTEnergyStorage? = when (storage) {
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
