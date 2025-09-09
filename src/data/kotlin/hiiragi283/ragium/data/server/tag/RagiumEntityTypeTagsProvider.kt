package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumEntityTypeTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<EntityType<*>>(
        output,
        Registries.ENTITY_TYPE,
        provider,
        helper,
    ) {
    override fun addTags(builder: HTTagBuilder<EntityType<*>>) {
        builder.addTag(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, Tags.EntityTypes.BOSSES)
        builder.addTag(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        builder.add(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS, HTHolderLike.fromEntity(EntityType.WARDEN))
        builder.add(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING, HTHolderLike.fromEntity(EntityType.WARDEN))
    }
}
