package hiiragi283.ragium.api.block.entity

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.UUIDUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.*

/**
 * 所有者を保持する[BlockEntity]
 */
interface HTPlayerOwningBlockEntity {
    companion object {
        @JvmField
        val UUID_CODEC: Codec<Optional<UUID>> = ExtraCodecs.optionalEmptyMap(UUIDUtil.STRING_CODEC)
    }

    /**
     * 所有者の[UUID]を返します。
     */
    val ownerUUID: UUID?

    /**
     * 現在のサーバーと[ownerUUID]から，所有者の[ServerPlayer]を返します。
     */
    val owner: ServerPlayer?
        get() = RagiumAPI.getInstance().getPlayer(ownerUUID)
}
