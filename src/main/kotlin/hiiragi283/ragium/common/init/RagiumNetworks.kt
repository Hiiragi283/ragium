package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

object RagiumNetworks {
    @JvmStatic
    val SET_STACK: CustomPayload.Id<HTInventoryPayload.Setter> =
        registerS2C("set_stack", HTInventoryPayload.Setter.PACKET_CODEC)

    @JvmStatic
    val REMOVE_STACK: CustomPayload.Id<HTInventoryPayload.Remover> =
        registerS2C("remove_stack", HTInventoryPayload.Remover.PACKET_CODEC)

    @JvmStatic
    private fun <T : CustomPayload> registerS2C(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(Ragium.id(name))
        PayloadTypeRegistry.playS2C().register(id, codec)
        return id
    }

    @JvmStatic
    private fun <T : CustomPayload> registerC2S(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(Ragium.id(name))
        PayloadTypeRegistry.playS2C().register(id, codec)
        return id
    }
}
