package hiiragi283.ragium.setup

import hiiragi283.ragium.api.network.HTCustomPayload
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.registration.PayloadRegistrar

class RagiumPayloadRegister(private val registrar: PayloadRegistrar) {
    fun <T : HTCustomPayload.S2C> registerS2C(type: CustomPacketPayload.Type<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>) {
        registrar.playToClient(type, streamCodec, HTCustomPayload.S2C::handle)
    }

    fun <T : HTCustomPayload.C2S> registerC2S(type: CustomPacketPayload.Type<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>) {
        registrar.playToServer(type, streamCodec, HTCustomPayload.C2S::handle)
    }
}
