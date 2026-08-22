package hiiragi283.ragium.network

import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.network.HTCustomPayload
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.gui.menu.HTContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class HTUpdateMenuPacket private constructor(val containerId: Int, val map: Map<Int, HTSyncablePayload>) :
    HTCustomPayload.S2C,
    HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<HTUpdateMenuPacket> = CustomPacketPayload.Type(RagiumAPI.id("update_menu"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateMenuPacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTUpdateMenuPacket::containerId,
            ByteBufCodecs.map(::HashMap, ByteBufCodecs.VAR_INT, HTSyncablePayload.STREAM_CODEC),
            HTUpdateMenuPacket::map,
            ::HTUpdateMenuPacket,
        )

        @JvmStatic
        fun create(containerId: Int, map: Map<Int, HTSyncablePayload>): HTUpdateMenuPacket? = when {
            map.isEmpty() -> null
            else -> HTUpdateMenuPacket(containerId, map)
        }

        @JvmStatic
        inline fun create(containerId: Int, builderAction: MutableMap<Int, HTSyncablePayload>.() -> Unit): HTUpdateMenuPacket? = create(containerId, buildMap(builderAction))
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateMenuPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        handle(player)
    }

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        handle(player)
    }

    private fun handle(player: Player) {
        val container: HTContainerMenu<*> = player.containerMenu as? HTContainerMenu<*> ?: return
        if (container.containerId == this.containerId) {
            for ((index: Int, payload: HTSyncablePayload) in map) {
                payload.setValue(container, index)
                RagiumAPI.LOGGER.debug("Received sync value index: {}, payload: {}", index, payload)
            }
        }
    }
}
