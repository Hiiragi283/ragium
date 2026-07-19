package hiiragi283.ragium.setup

import hiiragi283.ragium.common.item.HTUniversalChestManager
import net.neoforged.neoforge.attachment.AttachmentType

data object RagiumAttachmentTypes {
    @JvmField
    val UNIVERSAL_CHEST: AttachmentType<HTUniversalChestManager> = AttachmentType.serializable(::HTUniversalChestManager).build()
}
