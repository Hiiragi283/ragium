package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.attachment.HTValueAttachmentSerializer
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.api.storage.value.HTValueSerializable
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier

class HTDeferredAttachmentRegister(namespace: String) :
    HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, supplier: Supplier<AttachmentType.Builder<TYPE>>): HTDeferredAttachmentType<TYPE> {
        val holder: HTDeferredAttachmentType<TYPE> = HTDeferredAttachmentType.createType(createId(name))
        register(name) { _: ResourceLocation -> supplier.get().build() }
        return holder
    }

    fun <TYPE : HTValueSerializable> registerSerializable(
        name: String,
        factory: Function<IAttachmentHolder, TYPE>,
    ): HTDeferredAttachmentType<TYPE> = registerType(name) {
        AttachmentType.builder(factory).serialize(Serializer(factory))
    }

    fun <TYPE : HTValueSerializable> registerSerializable(name: String, supplier: Supplier<TYPE>): HTDeferredAttachmentType<TYPE> =
        registerType(name) {
            AttachmentType.builder(supplier).serialize(Serializer(supplier))
        }

    private class Serializer<T : HTValueSerializable>(private val factory: Function<IAttachmentHolder, T>) :
        HTValueAttachmentSerializer<T> {
        constructor(factory: Supplier<T>) : this(Function { factory.get() })

        override fun read(holder: IAttachmentHolder, input: HTValueInput): T = factory.apply(holder).apply { deserialize(input) }

        override fun write(attachment: T, output: HTValueOutput) {
            attachment.serialize(output)
        }
    }
}
