package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.inventory.HTPotionBundleContainerMenu
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext

data object HTPotionBundlePacket : CustomPacketPayload {
    @JvmField
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPotionBundlePacket> =
        StreamCodec.unit(HTPotionBundlePacket)

    @JvmField
    val TYPE = CustomPacketPayload.Type<HTPotionBundlePacket>(RagiumAPI.id("potion_bundle"))

    @JvmStatic
    fun onReceived(payload: HTPotionBundlePacket, context: IPayloadContext) {
        context.player().openMenu(
            SimpleMenuProvider(
                { containerId: Int, inventory: Inventory, _: Player ->
                    HTPotionBundleContainerMenu(
                        containerId,
                        inventory,
                    )
                },
                RagiumItems.POTION_BUNDLE.get().description,
            ),
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
