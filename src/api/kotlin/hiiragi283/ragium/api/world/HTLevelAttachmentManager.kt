package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.neoforge.attachment.AttachmentType

class HTLevelAttachmentManager<T : Any>(private val attachmentType: AttachmentType<T>) {
    fun get(level: Level?): T? = when (level) {
        is ServerLevel -> getFromServer(level)

        else -> level?.dimension()?.let(::getFromKey)
    }

    fun getFromKey(key: ResourceKey<Level>): T? = RagiumAPI
        .getInstance()
        .getCurrentServer()
        ?.getLevel(key)
        ?.let(::getFromServer)

    fun getFromServer(level: ServerLevel): T = level.getData(attachmentType)
}
