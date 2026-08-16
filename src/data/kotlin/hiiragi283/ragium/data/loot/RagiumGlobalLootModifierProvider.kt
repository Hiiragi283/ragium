package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTGlobalLootModifierProvider
import hiiragi283.ragium.api.RagiumAPI
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType

class RagiumGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : HTGlobalLootModifierProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        // Drops Elder Heart from Elder Guardian
        add(RagiumGlobalLootTableProvider.ELDER_HEART, condition(EntityType.ELDER_GUARDIAN).toList())
        // Drops Trader Catalog from Wandering Trader
        add(RagiumGlobalLootTableProvider.TRADER_CATALOG, condition(EntityType.WANDERING_TRADER).toList())
    }
}
