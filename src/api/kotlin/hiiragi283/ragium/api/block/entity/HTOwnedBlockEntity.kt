package hiiragi283.ragium.api.block.entity

import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.common.UsernameCache
import net.neoforged.neoforge.common.util.FakePlayerFactory
import java.util.UUID

/**
 * 所有者を保持する[BlockEntity]向けのインターフェース
 * @see net.minecraft.world.entity.OwnableEntity
 */
fun interface HTOwnedBlockEntity {
    companion object {
        @JvmField
        val FAKE_PROFILE = GameProfile(UUID.fromString("32fe5dc2-f03b-4230-baf2-1ffc07d3d818"), "[Ragium]")
    }

    fun getOwner(): UUID?

    fun getOwnerName(): String = getOwner()?.let(UsernameCache::getLastKnownUsername) ?: "???"

    fun getOwnerPlayer(level: ServerLevel): ServerPlayer? = getOwnerPlayer(level.server)

    fun getOwnerPlayer(server: MinecraftServer): ServerPlayer? = getOwner()?.let(server.playerList::getPlayer)

    private fun getOwnerProfile(): GameProfile {
        val owner: UUID = getOwner() ?: return FAKE_PROFILE
        return GameProfile(owner, getOwnerName())
    }

    fun getFakePlayer(level: ServerLevel): ServerPlayer = FakePlayerFactory.get(level, getOwnerProfile())

    fun getOwnerOrFake(level: ServerLevel): ServerPlayer = getOwnerPlayer(level) ?: getFakePlayer(level)
}
