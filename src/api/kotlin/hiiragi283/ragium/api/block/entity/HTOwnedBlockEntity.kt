package hiiragi283.ragium.api.block.entity

import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.common.UsernameCache
import java.util.UUID

/**
 * 所有者を保持する[BlockEntity]向けのインターフェース
 * @see net.minecraft.world.entity.OwnableEntity
 */
fun interface HTOwnedBlockEntity {
    fun getOwner(): UUID?

    fun getOwnerName(): String = getOwner()?.let(UsernameCache::getLastKnownUsername) ?: "???"
}
