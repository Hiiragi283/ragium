package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.core.HolderLookup

class RagiumBlockLootTableProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        knownBlocks.forEach(::dropSelf)
    }
}
