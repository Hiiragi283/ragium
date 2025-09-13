package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentType
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.common.storage.item.HTUniversalBundleManager

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(RagiumAPI.MOD_ID)

    @JvmField
    internal val UNIVERSAL_BUNDLE: HTDeferredAttachmentType<HTUniversalBundleManager> =
        REGISTER.registerSerializable("universal_bundle", ::HTUniversalBundleManager)

    @JvmField
    internal val ENERGY_NETWORK: HTDeferredAttachmentType<HTEnergyNetwork> =
        REGISTER.registerSerializable("energy_network", ::HTEnergyNetwork)
}
