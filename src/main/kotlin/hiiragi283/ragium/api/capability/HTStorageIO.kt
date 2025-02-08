package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.RagiumAPI
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

    /**
     * 指定した[handler]の搬入出を制限した[IItemHandlerModifiable]を返します。
     */
    fun wrapItemHandler(handler: IItemHandlerModifiable): IItemHandlerModifiable = RagiumAPI.getInstance().wrapItemHandler(this, handler)

    /**
     * 指定した[handler]の搬入出を制限した[IFluidHandler]を返します。
     */
    fun wrapFluidHandler(handler: IFluidHandler): IFluidHandler = RagiumAPI.getInstance().wrapFluidHandler(this, handler)

    /**
     * 指定した[storage]の搬入出を制限した[IEnergyStorage]を返します。
     */
    fun wrapEnergyStorage(storage: IEnergyStorage): IEnergyStorage = RagiumAPI.getInstance().wrapEnergyStorage(this, storage)
}
