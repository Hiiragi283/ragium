package hiiragi283.ragium.api.energy

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.DefaultedRegistry
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry

class HTEnergyType : TransferVariant<HTEnergyType> {
    companion object {
        @JvmField
        val REGISTRY_KEY: RegistryKey<Registry<HTEnergyType>> = RegistryKey.ofRegistry(RagiumAPI.id("energy_type"))

        @JvmField
        val REGISTRY: DefaultedRegistry<HTEnergyType> =
            FabricRegistryBuilder
                .createDefaulted(REGISTRY_KEY, RagiumAPI.id("empty"))
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister()

        @JvmField
        val CODEC: Codec<HTEnergyType> = Codec.lazyInitialized(REGISTRY::getCodec)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTEnergyType> = PacketCodecs.registryCodec(CODEC)

        @JvmField
        val ENTRY_CODEC: Codec<RegistryEntry<HTEnergyType>> =
            Codec.lazyInitialized(REGISTRY::getEntryCodec)

        @JvmField
        val ENTRY_PACKET_CODEC: PacketCodec<RegistryByteBuf, RegistryEntry<HTEnergyType>> =
            PacketCodecs.registryEntry(REGISTRY_KEY)

        @JvmField
        val EMPTY: HTEnergyType = register("empty")

        @JvmField
        val HEAT: HTEnergyType = register("heat")

        @JvmField
        val ELECTRICITY: HTEnergyType = register("electricity")

        @JvmStatic
        private fun register(name: String): HTEnergyType = Registry.register(
            REGISTRY,
            RagiumAPI.id(name),
            HTEnergyType(),
        )
    }

    override fun isBlank(): Boolean = this == EMPTY

    override fun getObject(): HTEnergyType = this

    override fun getComponents(): ComponentChanges = ComponentChanges.EMPTY

    override fun getComponentMap(): ComponentMap = ComponentMap.EMPTY
}
