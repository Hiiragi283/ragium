package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags

@Suppress("DEPRECATION")
class RagiumEntityTypeTagsProvider(context: HTDataGenContext) : HTTagsProvider<EntityType<*>>(Registries.ENTITY_TYPE, context) {
    override fun addTags(builder: HTTagBuilder<EntityType<*>>) {
        builder.addTag(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, Tags.EntityTypes.BOSSES)
        builder.addTag(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        builder.add(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS, EntityType.WARDEN.toHolderLike())
        builder.add(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING, EntityType.WARDEN.toHolderLike())
    }
}
