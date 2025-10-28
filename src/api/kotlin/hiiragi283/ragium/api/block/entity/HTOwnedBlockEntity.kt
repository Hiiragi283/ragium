package hiiragi283.ragium.api.block.entity

import net.neoforged.neoforge.common.UsernameCache
import java.util.UUID

/**
 * @see net.minecraft.world.entity.OwnableEntity
 */
fun interface HTOwnedBlockEntity {
    fun getOwner(): UUID?

    fun getOwnerName(): String = getOwner()?.let(UsernameCache::getLastKnownUsername) ?: "???"
}
