package hiiragi283.ragium.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup

class RagiumBlockLootProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries) {
    override fun generate() {
        RagiumBlocks.REGISTER.asBlockSequence().forEach(::dropSelf)
    }
}
