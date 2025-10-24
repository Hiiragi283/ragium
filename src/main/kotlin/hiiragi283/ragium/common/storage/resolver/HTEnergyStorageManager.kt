package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTSidedEnergyStorage
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.common.storage.proxy.HTProxyEnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * @see [mekanism.common.capabilities.resolver.manager.EnergyHandlerManager]
 */
class HTEnergyStorageManager(holder: HTEnergyBatteryHolder?, baseHandler: HTSidedEnergyStorage) :
    HTCapabilityManagerImpl<HTEnergyBatteryHolder, HTEnergyBattery, IEnergyStorage, HTSidedEnergyStorage>(
        holder,
        baseHandler,
        ::HTProxyEnergyStorage,
        HTEnergyBatteryHolder::getEnergyBatteries,
    )
