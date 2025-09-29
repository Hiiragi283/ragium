package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentType
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.common.storage.item.HTMachineUpgradeItemHandler
import hiiragi283.ragium.common.storage.item.HTUniversalBundleManager

internal object RagiumAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(RagiumAPI.MOD_ID)

    @JvmField
    val UNIVERSAL_BUNDLE: HTDeferredAttachmentType<HTUniversalBundleManager> =
        REGISTER.registerSerializable("universal_bundle", ::HTUniversalBundleManager)

    @JvmField
    val ENERGY_NETWORK: HTDeferredAttachmentType<HTEnergyNetwork> =
        REGISTER.registerSerializable("energy_network", ::HTEnergyNetwork)

    @JvmField
    val MACHINE_UPGRADE: HTDeferredAttachmentType<HTMachineUpgradeItemHandler> =
        REGISTER.registerSerializable("machine_upgrade", HTMachineUpgradeItemHandler::fromHolder)
}
