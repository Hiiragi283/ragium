package hiiragi283.ragium.data.loot

import hiiragi283.lib.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.item.RagiumItems
import java.util.function.BiConsumer
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

sealed class RagiumGlobalLootTableProvider(protected val provider: HolderLookup.Provider) : LootTableSubProvider {
    companion object {
        // entity
        @JvmField
        val ELDER_HEART: ResourceKey<LootTable> = create("elder_heart")

        @JvmField
        val TRADER_CATALOG: ResourceKey<LootTable> = create("trader_catalog")

        @JvmStatic
        private fun create(path: String): ResourceKey<LootTable> = create(RagiumAPI.id(path))

        @JvmStatic
        private fun create(id: Identifier): ResourceKey<LootTable> = Registries.LOOT_TABLE.createKey(id.withPath { "drop_$it" })
    }

    //    EntityProvider    //

    class EntityProvider(provider: HolderLookup.Provider) : RagiumGlobalLootTableProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Elder Heart from Elder Guardian
            output.accept(
                ELDER_HEART,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(
                                LootItem
                                    .lootTableItem(RagiumItems.ELDER_HEART)
                                    .apply(
                                        EnchantedCountIncreaseFunction.lootingMultiplier(
                                            provider,
                                            UniformGenerator.between(0f, 1f),
                                        ),
                                    ),
                            ),
                    ),
            )
            // Drops Trader Catalog from Wandering Trader
            /*output.accept(
                TRADER_CATALOG,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(LootItem.lootTableItem(HCItems.TRADER_CATALOG)),
                    ),
            )*/
        }
    }
}
