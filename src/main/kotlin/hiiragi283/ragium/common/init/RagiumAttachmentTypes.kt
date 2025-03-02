package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RagiumAPI.MOD_ID)

    @JvmField
    val DYNAMITE_POWER: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
        REGISTER.register("dynamite_power") { _: ResourceLocation ->
            AttachmentType
                .builder<Float> { holder: IAttachmentHolder ->
                    if (holder is Entity) 2f else throw UnsupportedOperationException("Only used for entity!")
                }.serialize(Codec.floatRange(0f, 16f))
                .build()
        }
}
