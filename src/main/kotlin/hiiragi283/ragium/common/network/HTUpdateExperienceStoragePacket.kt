package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTExperienceScreen
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
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
data class HTUpdateExperienceStoragePacket private constructor(val pos: BlockPos, val amount: Long) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<HTUpdateExperienceStoragePacket> =
            CustomPacketPayload.Type(RagiumAPI.id("update_experience_storage"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateExperienceStoragePacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateExperienceStoragePacket::pos,
            ByteBufCodecs.VAR_LONG,
            HTUpdateExperienceStoragePacket::amount,
            ::HTUpdateExperienceStoragePacket,
        )

        @JvmStatic
        fun create(blockEntity: HTBlockEntity): HTUpdateExperienceStoragePacket? {
            val tank: HTExperienceTank = blockEntity.getExpTank(0, null) ?: return null
            return HTUpdateExperienceStoragePacket(blockEntity.blockPos, tank.getAmount())
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateExperienceStoragePacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val screen: HTExperienceScreen = minecraft.screen as? HTExperienceScreen ?: return
        if (!screen.checkPosition(pos)) return
        screen.getExperienceWidget()?.setAmount(amount)
    }
}
