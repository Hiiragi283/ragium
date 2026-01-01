package hiiragi283.ragium.setup

import hiiragi283.core.common.registry.HTDeferredAttachmentType
import hiiragi283.core.common.registry.register.HTDeferredAttachmentRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.tool.HTUniversalChestManager

internal object RagiumAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(RagiumAPI.MOD_ID)

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredAttachmentType<HTUniversalChestManager> =
        REGISTER.registerSerializable("universal_chest", ::HTUniversalChestManager)
}
