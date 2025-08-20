package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : Any> register(name: String, supplier: () -> T, codec: BiCodec<in RegistryFriendlyByteBuf, T>): Supplier<AttachmentType<T>> =
        REGISTER.register(name) { _: ResourceLocation ->
            AttachmentType
                .builder(supplier)
                .sync(codec.streamCodec)
                .serialize(codec.codec)
                .build()
        }

    @JvmField
    val ENERGY_NETWORK: Supplier<AttachmentType<HTEnergyNetwork>> =
        register("energy_network", ::HTEnergyNetwork, HTEnergyNetwork.CODEC)
}
