package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * ストレージの搬入出を管理するクラス
 */
enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) : StringRepresentable {
    /**
     * 搬入出を受け付けない
     */
    EMPTY(false, false),

    /**
     * 触媒スロット
     */
    CATALYST(false, false),

    /**
     * ホッパーやパイプを介した搬入
     */
    INPUT(true, false),

    /**
     * ホッパーやパイプを介した搬出
     */
    OUTPUT(false, true),

    /**
     * ホッパーやパイプを介した搬入出の両方を受け付ける
     */
    GENERIC(true, true),
    ;

    /**
     * 指定した[tank]の搬入出を制限した[IFluidHandler]を返します。
     */
    fun wrapFluidTank(tank: HTFluidTank): IFluidHandler = RagiumAPI.getInstance().wrapFluidTank(this, tank)

    /**
     * 指定した[storage]の搬入出を制限した[IEnergyStorage]を返します。
     */
    fun wrapEnergyStorage(storage: IEnergyStorage): IEnergyStorage = RagiumAPI.getInstance().wrapEnergyStorage(this, storage)

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
