package hiiragi283.ragium.api.storage.energy

import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * エネルギーネットワークを管理するマネージャ
 */
interface HTEnergyNetworkManager {
    /**
     * 指定した[level]からエネルギーネットワークを返します。
     * @return 対応するネットワークがない場合は`null`
     */
    fun getNetwork(level: Level?): IEnergyStorage?

    /**
     * 指定した[key]からエネルギーネットワークを返します。
     * @return 対応するネットワークがない場合は`null`
     */
    fun getNetworkFromKey(key: ResourceKey<Level>): IEnergyStorage?

    /**
     * 指定した[level]からエネルギーネットワークを返します。
     */
    fun getNetworkFromServer(level: ServerLevel): IEnergyStorage
}
