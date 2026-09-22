package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.world.RagiumDamageTypes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import java.util.concurrent.CompletableFuture

class RagiumDamageTypeTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTTagsProvider<DamageType>(output, Registries.DAMAGE_TYPE, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        builder(DamageTypeTags.BYPASSES_ARMOR).add(RagiumDamageTypes.CHEMICAL_BURN)
        builder(DamageTypeTags.NO_KNOCKBACK).add(RagiumDamageTypes.CHEMICAL_BURN)
        builder(DamageTypeTags.PANIC_CAUSES).add(RagiumDamageTypes.CHEMICAL_BURN)
    }
}
