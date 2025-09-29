package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.data.HTDataGenContext
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet

object RagiumLootTableProvider {
    @JvmStatic
    fun create(
        vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, LootContextParamSet>,
    ): HTDataGenContext.Factory<LootTableProvider> = HTDataGenContext.Factory { context: HTDataGenContext ->
        LootTableProvider(
            context.output,
            setOf(),
            pairs.map { (function: (HolderLookup.Provider) -> LootTableSubProvider, set: LootContextParamSet) ->
                SubProviderEntry(function, set)
            },
            context.registries,
        )
    }
}
