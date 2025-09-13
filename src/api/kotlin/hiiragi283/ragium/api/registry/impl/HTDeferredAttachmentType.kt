package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredAttachmentType<TYPE : Any>(key: ResourceKey<AttachmentType<*>>) :
    HTDeferredHolder<AttachmentType<*>, AttachmentType<TYPE>>(key) {
    companion object {
        @JvmStatic
        fun <TYPE : Any> createType(key: ResourceLocation): HTDeferredAttachmentType<TYPE> =
            createType(ResourceKey.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, key))

        @JvmStatic
        fun <TYPE : Any> createType(key: ResourceKey<AttachmentType<*>>): HTDeferredAttachmentType<TYPE> = HTDeferredAttachmentType(key)
    }

    fun hasData(holder: IAttachmentHolder): Boolean = holder.hasData(this)

    fun getData(holder: IAttachmentHolder): TYPE = holder.getData(this)

    fun getExistingData(holder: IAttachmentHolder): TYPE? = holder.getExistingDataOrNull(this)

    fun setData(holder: IAttachmentHolder, data: TYPE): TYPE? = holder.setData(this, data)

    fun removeData(holder: IAttachmentHolder): TYPE? = holder.removeData(this)
}
