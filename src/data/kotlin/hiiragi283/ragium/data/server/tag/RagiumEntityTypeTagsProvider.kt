package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagDependType
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags

@Suppress("DEPRECATION")
class RagiumEntityTypeTagsProvider(context: HTDataGenContext) : HTTagsProvider<EntityType<*>>(Registries.ENTITY_TYPE, context) {
    override fun addTagsInternal(factory: BuilderFactory<EntityType<*>>) {
        factory
            .apply(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST)
            .addTag(Tags.EntityTypes.BOSSES)
            .addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        factory.apply(RagiumModTags.EntityTypes.CONFUSION_BLACKLIST).addEntity(EntityType.CREEPER)

        factory.apply(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS).addEntity(EntityType.WARDEN)

        factory.apply(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING).addEntity(EntityType.WARDEN)
    }

    private fun HTTagBuilder<EntityType<*>>.addEntity(entityType: EntityType<*>, type: HTTagDependType = HTTagDependType.REQUIRED) {
        add(entityType.toHolderLike(), type)
    }
}
