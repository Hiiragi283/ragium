package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.common.block.entity.ExtendedBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.Level

/**
 * @see mekanism.common.network.to_client.PacketUpdateTile
 */
@ConsistentCopyVisibility
@JvmRecord
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
        fun create(blockEntity: ExtendedBlockEntity): HTUpdateBlockEntityPacket? {
            val access: RegistryAccess = blockEntity.getRegistryAccess() ?: return null
            return HTUpdateBlockEntityPacket(blockEntity.getBlockPos(), blockEntity.getReducedUpdateTag(access))
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateBlockEntityPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val level: Level = player.level()
        level.getBlockEntity(pos)?.handleUpdateTag(updateTag, level.registryAccess())
    }
}
