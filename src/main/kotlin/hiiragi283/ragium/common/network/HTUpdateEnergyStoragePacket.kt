package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTEnergyScreen
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@ConsistentCopyVisibility
@JvmRecord
data class HTUpdateEnergyStoragePacket private constructor(val pos: BlockPos, val amount: Int) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<HTUpdateEnergyStoragePacket> =
            CustomPacketPayload.Type(RagiumAPI.id("update_energy_storage"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateEnergyStoragePacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateEnergyStoragePacket::pos,
            ByteBufCodecs.VAR_INT,
            HTUpdateEnergyStoragePacket::amount,
            ::HTUpdateEnergyStoragePacket,
        )

        @JvmStatic
        fun create(blockEntity: HTBlockEntity): HTUpdateEnergyStoragePacket? {
            val battery: HTEnergyBattery = blockEntity.getEnergyBattery(blockEntity.getEnergySideFor()) ?: return null
            return HTUpdateEnergyStoragePacket(blockEntity.blockPos, battery.getAmount())
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateEnergyStoragePacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val screen: HTEnergyScreen = minecraft.screen as? HTEnergyScreen ?: return
        if (!screen.checkPosition(pos)) return
        screen.getEnergyWidget()?.setAmount(amount)
    }
}
