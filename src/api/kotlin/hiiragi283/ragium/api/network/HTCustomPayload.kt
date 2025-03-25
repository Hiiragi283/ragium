package hiiragi283.ragium.api.network

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

interface HTCustomPayload : CustomPacketPayload {
    fun handle(context: IPayloadContext)
}
