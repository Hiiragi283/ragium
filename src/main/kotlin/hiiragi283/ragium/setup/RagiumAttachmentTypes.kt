package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTDeferredAttachmentRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.item.HTUniversalChestManager
import net.neoforged.neoforge.attachment.AttachmentType

data object RagiumAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(RagiumAPI.MOD_ID)

    @JvmField
    val UNIVERSAL_CHEST: AttachmentType<HTUniversalChestManager> =
        REGISTER.registerType(RagiumConst.UNIVERSAL_CHEST, AttachmentType.serializable(::HTUniversalChestManager))
}
