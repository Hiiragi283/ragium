package hiiragi283.ragium.api.block.entity

import net.neoforged.neoforge.common.UsernameCache
import java.util.UUID

fun interface HTOwnedBlockEntity {
    fun getOwnerUUID(): UUID

    fun getLastOwnerName(): String = UsernameCache.getLastKnownUsername(getOwnerUUID()) ?: "???"
}
