package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.experience.HTSidedExperienceHandler
import hiiragi283.ragium.api.storage.experience.IExperienceHandler
import hiiragi283.ragium.api.storage.holder.HTExperienceTankHolder
import hiiragi283.ragium.common.storage.proxy.HTProxyExperienceHandler

class HTExperienceHandlerManager(holder: HTExperienceTankHolder?, baseHandler: HTSidedExperienceHandler) :
    HTCapabilityManagerImpl<HTExperienceTankHolder, HTExperienceTank, IExperienceHandler, HTSidedExperienceHandler>(
        holder,
        baseHandler,
        ::HTProxyExperienceHandler,
        HTExperienceTankHolder::getExperienceTank,
    )
