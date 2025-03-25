package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.network.HTCustomPayload
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class HTBlockEntityUpdatePacket(val pos: BlockPos, val updateTag: CompoundTag) : HTCustomPayload {
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

    override fun type(): CustomPacketPayload.Type<HTBlockEntityUpdatePacket> = TYPE

    override fun handle(context: IPayloadContext) {
        val level: Level = context.player().level()
        val blockEntity: BlockEntity? = level.getBlockEntity(pos)
        if (blockEntity is HTBlockEntity) {
            blockEntity.handleUpdateTag(updateTag, level.registryAccess())
        }
    }
}
