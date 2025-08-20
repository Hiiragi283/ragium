package hiiragi283.ragium.api.world

import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level

interface HTSavedDataManager<T : Any> {
    fun get(level: Level?): T? = when (level) {
        is ServerLevel -> getFromServer(level)

        else -> level?.dimension()?.let(::getFromKey)
    }

    fun getFromKey(key: ResourceKey<Level>): T?

    fun getFromServer(level: ServerLevel): T
}
