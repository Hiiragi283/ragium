package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredAttachmentType<TYPE : Any> : HTDeferredHolder<AttachmentType<*>, AttachmentType<TYPE>> {
    constructor(key: ResourceKey<AttachmentType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, id)

    fun hasData(holder: IAttachmentHolder): Boolean = holder.hasData(this)

    fun getData(holder: IAttachmentHolder): TYPE = holder.getData(this)

    fun getExistingData(holder: IAttachmentHolder): TYPE? = holder.getExistingDataOrNull(this)

    fun setData(holder: IAttachmentHolder, data: TYPE): TYPE? = holder.setData(this, data)

    fun removeData(holder: IAttachmentHolder): TYPE? = holder.removeData(this)
}
