package hiiragi283.ragium.api.block.entity

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.UUIDUtil
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.entity.player.Player
import java.util.*

interface HTPlayerOwningBlockEntity {
    companion object {
        @JvmField
        val UUID_CODEC: Codec<Optional<UUID>> = ExtraCodecs.optionalEmptyMap(UUIDUtil.STRING_CODEC)
    }

    val ownerUUID: UUID?

    val owner: Player?
        get() = RagiumAPI.getInstance().getPlayer(ownerUUID)
}
