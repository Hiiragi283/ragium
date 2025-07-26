package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.network.handling.IPayloadContext

class HTFluidSlotUpdatePacket(val index: Int, val stack: FluidStack) : HTCustomPayload {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTFluidSlotUpdatePacket>(RagiumAPI.id("fluid_slot_update"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidSlotUpdatePacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTFluidSlotUpdatePacket::index,
            FluidStack.OPTIONAL_STREAM_CODEC,
            HTFluidSlotUpdatePacket::stack,
            ::HTFluidSlotUpdatePacket,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTFluidSlotUpdatePacket> = TYPE

    override fun handle(context: IPayloadContext) {
        val screen: Screen = Minecraft.getInstance().screen ?: return
        if (screen is HTDefinitionContainerScreen<*>) {
            screen.setFluidStack(index, stack)
        }
    }
}
