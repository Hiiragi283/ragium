package hiiragi283.ragium.network

import com.mojang.logging.LogUtils
import hiiragi283.lib.network.HTCustomPayload
import hiiragi283.lib.util.printError
import hiiragi283.lib.world.getBlockEntityResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.block.entity.HTExtendedBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.TagValueInput
import net.minecraft.world.level.storage.ValueInput
import org.slf4j.Logger

/**
 * [HTExtendedBlockEntity]におけるサーバー側からクライアント側への同期に使用されるパケットのクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTUpdateBlockEntityPacket private constructor(val pos: BlockPos, val updateTag: CompoundTag) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

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
        fun create(blockEntity: HTExtendedBlockEntity): HTUpdateBlockEntityPacket? = blockEntity.getRegistryAccess().map { HTUpdateBlockEntityPacket(blockEntity.blockPos, blockEntity.createReducedUpdateTag(it)) }.getOrNull()
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateBlockEntityPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val level: Level = player.level()
        level.getBlockEntityResult<HTExtendedBlockEntity>(pos)
            .printError(LOGGER)
            .onRight { blockEntity: HTExtendedBlockEntity ->
                val input: ValueInput = TagValueInput.create(blockEntity.createReporter(), level.registryAccess(), updateTag)
                blockEntity.handleUpdateTag(input)
            }
    }
}
