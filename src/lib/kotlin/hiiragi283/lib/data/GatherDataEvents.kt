package hiiragi283.lib.data

import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.util.context.ContextKeySet
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * この[GatherDataEvent][this]に[LootTableProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun GatherDataEvent.createLootTables(
    vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, ContextKeySet>,
): LootTableProvider = this.createProvider { output: PackOutput, future: CompletableFuture<HolderLookup.Provider> ->
    LootTableProvider(
        output,
        emptySet(),
        pairs.map { LootTableProvider.SubProviderEntry(it.first, it.second) },
        future,
    )
}
