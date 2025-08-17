package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumDamageTypeTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<DamageType>(
        output,
        Registries.DAMAGE_TYPE,
        provider,
        helper,
    ) {
    override fun addTags(builder: HTTagBuilder<DamageType>) {
        builder.add(RagiumCommonTags.DamageTypes.IS_SONIC, DamageTypes.SONIC_BOOM)
    }
}
