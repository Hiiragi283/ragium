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

data class HTBlockEntityUpdatePacket(val pos: BlockPos, val updateTag: CompoundTag) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTBlockEntityUpdatePacket>(RagiumAPI.id("block_entity_update"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBlockEntityUpdatePacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTBlockEntityUpdatePacket::pos,
            ByteBufCodecs.COMPOUND_TAG,
            HTBlockEntityUpdatePacket::updateTag,
            ::HTBlockEntityUpdatePacket,
        )
    }

    constructor(blockEntity: HTBlockEntityExtension) : this(
        blockEntity.getBlockPos(),
        blockEntity.getLevel()?.registryAccess()?.let(blockEntity::getReducedUpdateTag) ?: CompoundTag(),
    )

    override fun type(): CustomPacketPayload.Type<HTBlockEntityUpdatePacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        with(player.level()) {
            getBlockEntity(pos)?.handleUpdateTag(updateTag, registryAccess())
        }
    }
}
