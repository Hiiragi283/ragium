package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RagiumAPI.MOD_ID)

    @JvmField
    val ENERGY_NETWORK: Supplier<AttachmentType<HTEnergyNetwork>> =
        REGISTER.register("energy_network") { _: ResourceLocation ->
            AttachmentType.serializable(::HTEnergyNetwork).build()
        }
}
