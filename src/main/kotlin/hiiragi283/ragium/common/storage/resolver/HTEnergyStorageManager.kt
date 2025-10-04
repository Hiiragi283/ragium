package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTSidedEnergyStorage
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.common.storage.proxy.HTProxyEnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * @see [mekanism.common.capabilities.resolver.manager.EnergyHandlerManager]
 */
class HTEnergyStorageManager(holder: HTEnergyStorageHolder?, baseHandler: HTSidedEnergyStorage) :
    HTCapabilityManagerImpl<HTEnergyStorageHolder, HTEnergyBattery, IEnergyStorage, HTSidedEnergyStorage>(
        holder,
        baseHandler,
        ::HTProxyEnergyStorage,
        { holder, side -> listOfNotNull(holder.getEnergyBattery(side)) },
    )
