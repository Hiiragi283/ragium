package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumCommonTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumDamageTypeTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    TagsProvider<DamageType>(
        output,
        Registries.DAMAGE_TYPE,
        provider,
        RagiumAPI.MOD_ID,
        helper,
    ) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(RagiumCommonTags.DamageTypes.IS_SONIC).add(DamageTypes.SONIC_BOOM)
    }
}
