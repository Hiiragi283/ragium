package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.builder.HTLootTableBuilder
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.function.BiConsumer

sealed class RagiumGlobalLootTableProvider(protected val provider: HolderLookup.Provider) : LootTableSubProvider {
    protected val fortune: Holder<Enchantment> by lazy { provider.getOrThrow(Enchantments.FORTUNE) }

    protected inline fun buildTable(builderAction: HTLootTableBuilder.() -> Unit): LootTable.Builder = HTLootTableBuilder.build(builderAction)

    //    ForBlock    //

    class ForBlock(provider: HolderLookup.Provider) : RagiumGlobalLootTableProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Extra minerals from ores
            // 砂利と同じドロップテーブル
            val extraDrop: LootItemCondition.Builder = BonusLevelTableCondition.bonusLevelFlatChance(
                fortune,
                0.1f,
                0.14285715f,
                0.25f,
                1.0f,
            )
            output.accept(
                RagiumGlobalLootTables.SULFUR_FROM_COAL_ORES,
                buildTable { pool { +item(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR).`when`(extraDrop) } },
            )
            output.accept(
                RagiumGlobalLootTables.CINNABAR_FROM_REDSTONE_ORES,
                buildTable { pool { +item(HTItemPart.GEM, RagiumMaterial.Gem.CINNABAR).`when`(extraDrop) } },
            )
            output.accept(
                RagiumGlobalLootTables.APATITE_FROM_LAPIS_ORES,
                buildTable { pool { +item(HTItemPart.GEM, RagiumMaterial.Gem.APATITE).`when`(extraDrop) } },
            )
            output.accept(
                RagiumGlobalLootTables.CARBON_FROM_DIAMOND_ORES,
                buildTable { pool { +item(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON).`when`(extraDrop) } },
            )
            output.accept(
                RagiumGlobalLootTables.FLUORITE_FROM_QUARTZ_ORES,
                buildTable { pool { +item(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE).`when`(extraDrop) } },
            )
        }

        private fun item(part: HTItemPart, material: RagiumMaterial): LootPoolSingletonContainer.Builder<*> =
            LootItem.lootTableItem(RagiumItems.getOrThrow(part, material))
    }

    //    ForEntity    //

    class ForEntity(provider: HolderLookup.Provider) : RagiumGlobalLootTableProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Elder Heart from Elder Guardian
            output.accept(
                RagiumGlobalLootTables.ELDER_HEART,
                buildTable {
                    pool {
                        +LootItem
                            .lootTableItem(RagiumItems.ELDER_HEART)
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0f, 1f)))
                    }
                },
            )
            // Drops Trader Catalog from Wandering Trader
            /*output.accept(
                RagiumGlobalLootTables.TRADER_CATALOG,
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
