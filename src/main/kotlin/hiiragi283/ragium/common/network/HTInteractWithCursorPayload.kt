package hiiragi283.ragium.common.network

import hiiragi283.lib.gui.widget.HTCursorInteractiveWidget
import hiiragi283.lib.network.HTCustomPayload
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.menu.HTWidgetContainerMenu
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

@JvmRecord
data class HTInteractWithCursorPayload(val containerId: Int, val widgetId: Int) : HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<HTInteractWithCursorPayload> = CustomPacketPayload.Type(RagiumAPI.id("interact_with_cursor"))

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTInteractWithCursorPayload> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTInteractWithCursorPayload::containerId,
            ByteBufCodecs.VAR_INT,
            HTInteractWithCursorPayload::widgetId,
            ::HTInteractWithCursorPayload,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTInteractWithCursorPayload> = TYPE

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val container: HTWidgetContainerMenu = player.containerMenu as? HTWidgetContainerMenu ?: return
        if (container.containerId == this.containerId) {
            val widget: HTCursorInteractiveWidget = container.widgetHolder.getWidget(this.widgetId) as? HTCursorInteractiveWidget ?: return
            widget.interactWithCursor(container.cursorAccess)
        }
    }
}
