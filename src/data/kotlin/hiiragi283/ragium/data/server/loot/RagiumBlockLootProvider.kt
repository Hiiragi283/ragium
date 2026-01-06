package hiiragi283.ragium.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class RagiumBlockLootProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries) {
    override fun generate() {
        RagiumBlocks.REGISTER.asBlockSequence().forEach(::dropSelf)

        // Ores
        for ((_, ore: HTSimpleDeferredBlock) in RagiumBlocks.MATERIALS.column(RagiumMaterial.RAGINITE)) {
            add(ore) {
                createOreDrops(
                    RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE),
                    UniformGenerator.between(4f, 5f),
                    it,
                )
            }
        }
        for ((prefix, ore: HTSimpleDeferredBlock) in RagiumBlocks.MATERIALS.column(RagiumMaterial.RAGI_CRYSTAL)) {
            add(ore) {
                createOreDrops(
                    RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.GEM, RagiumMaterial.RAGI_CRYSTAL),
                    null,
                    it,
                )
            }
        }
    }
}
