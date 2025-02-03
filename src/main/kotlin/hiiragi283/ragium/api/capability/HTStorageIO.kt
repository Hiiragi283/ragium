package hiiragi283.ragium.api.capability

import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

/**
 * ストレージの搬入出を管理するクラス
 */
enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    fun wrapItemHandler(handler: IItemHandlerModifiable): IItemHandlerModifiable = HTLimitedItemHandler(this, handler)

    fun wrapFluidHandler(handler: IFluidHandler): IFluidHandler = HTLimitedFluidHandler(this, handler)

    fun wrapEnergyStorage(storage: IEnergyStorage): IEnergyStorage = LimitedEnergyStorage(this, storage)
}
