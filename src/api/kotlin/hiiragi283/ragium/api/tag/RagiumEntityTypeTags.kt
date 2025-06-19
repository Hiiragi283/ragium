package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType

object RagiumEntityTypeTags {
    @JvmField
    val SENSITIVE_TO_NOISE_CANCELLING: TagKey<EntityType<*>> =
        TagKey.create(Registries.ENTITY_TYPE, RagiumAPI.id("sensitive_to_noise_cancelling"))
}
