package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTSidedFluidHandler
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.proxy.HTProxyFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * @see [mekanism.common.capabilities.resolver.manager.FluidHandlerManager]
 */
class HTFluidHandlerManager(holder: HTFluidTankHolder?, baseHandler: HTSidedFluidHandler) :
    HTCapabilityManagerImpl<HTFluidTankHolder, HTFluidTank, IFluidHandler, HTSidedFluidHandler>(
        holder,
        baseHandler,
        ::HTProxyFluidHandler,
        HTFluidTankHolder::getFluidTank,
    )
