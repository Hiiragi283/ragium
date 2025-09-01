package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityExtension
import hiiragi283.ragium.api.network.HTCustomPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.Level

/**
 * @see [mekanism.common.network.to_client.PacketUpdateTile]
 */
@ConsistentCopyVisibility
data class HTUpdateBlockEntityPacket private constructor(val pos: BlockPos, val updateTag: CompoundTag) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTUpdateBlockEntityPacket>(RagiumAPI.id("update_block_entity"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateBlockEntityPacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateBlockEntityPacket::pos,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG,
            HTUpdateBlockEntityPacket::updateTag,
            ::HTUpdateBlockEntityPacket,
        )

        @JvmStatic
        fun create(blockEntity: HTBlockEntityExtension): HTUpdateBlockEntityPacket? {
            val level: Level = blockEntity.getLevel() ?: return null
            return HTUpdateBlockEntityPacket(
                blockEntity.getBlockPos(),
                blockEntity.getReducedUpdateTag(level.registryAccess()),
            )
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateBlockEntityPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        with(player.level()) {
            getBlockEntity(pos)?.handleUpdateTag(updateTag, registryAccess())
        }
    }
}
