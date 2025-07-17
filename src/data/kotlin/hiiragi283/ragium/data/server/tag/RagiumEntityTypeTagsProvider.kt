package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumEntityTypeTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    IntrinsicHolderTagsProvider<EntityType<*>>(
        output,
        Registries.ENTITY_TYPE,
        provider,
        { entityType: EntityType<*> -> entityType.builtInRegistryHolder().key },
        RagiumAPI.MOD_ID,
        helper,
    ) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING)
            .add(EntityType.WARDEN)

        tag(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)
            .add(EntityType.WARDEN)
    }
}
