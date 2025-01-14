package hiiragi283.ragium.api.capability

import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * ストレージの搬入出を管理するクラス
 */
enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    fun wrapEnergyStorage(storage: IEnergyStorage): LimitedEnergyStorage = LimitedEnergyStorage(this, storage)
}
