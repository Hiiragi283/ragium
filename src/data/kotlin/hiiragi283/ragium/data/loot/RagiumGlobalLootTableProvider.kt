package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.getOrThrow
import hiiragi283.lib.data.loot.builder.HTLootTableBuilder
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders

sealed class RagiumGlobalLootTableProvider(protected val context: LootTableSubProvider.Context) : LootTableSubProvider {
    protected val fortune: Holder<Enchantment> by lazy { context.getOrThrow(Enchantments.FORTUNE) }

    protected inline fun buildTable(builderAction: HTLootTableBuilder.() -> Unit): LootTable.Builder =
        HTLootTableBuilder.build(builderAction)

    //    ForBlock    //

    class ForBlock(context: LootTableSubProvider.Context) : RagiumGlobalLootTableProvider(context) {
        override fun run() {
            // Extra minerals from ores
            context.accept(
                RagiumGlobalLootTables.SULFUR_FROM_COAL_ORES,
                buildTable {
                    pool {
                        +item(HTItemPart.DUST, RagiumMaterial.Mineral.SULFUR)
                            .apply(ApplyBonusCount.addOreBonusCount(fortune))
                    }
                }
            )
            context.accept(
                RagiumGlobalLootTables.CINNABAR_FROM_REDSTONE_ORES,
                buildTable {
                    pool {
                        +item(HTItemPart.GEM, RagiumMaterial.Gem.CINNABAR)
                            .apply(ApplyBonusCount.addOreBonusCount(fortune))
                    }
                }
            )
            context.accept(
                RagiumGlobalLootTables.FLUORITE_FROM_LAPIS_ORES,
                buildTable {
                    pool {
                        +item(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE)
                            .apply(ApplyBonusCount.addOreBonusCount(fortune))
                    }
                }
            )
            context.accept(
                RagiumGlobalLootTables.CARBON_FROM_DIAMOND_ORES,
                buildTable {
                    pool {
                        +item(HTItemPart.DUST, RagiumMaterial.Chemicals.CARBON)
                            .apply(ApplyBonusCount.addOreBonusCount(fortune))
                    }
                }
            )
        }

        private fun item(part: HTItemPart, material: RagiumMaterial): UniformContainerBase.Builder<*> =
            LootItem.lootTableItem(RagiumItems.getOrThrow(part, material))
    }

    //    ForEntity    //

    class ForEntity(context: LootTableSubProvider.Context) : RagiumGlobalLootTableProvider(context) {
        override fun run() {
            // Drops Elder Heart from Elder Guardian
            context.accept(
                RagiumGlobalLootTables.ELDER_HEART,
                buildTable {
                    pool {
                        +LootItem
                            .lootTableItem(RagiumItems.ELDER_HEART)
                            .apply(
                                EnchantedCountIncreaseFunction.Builder(
                                    context.getOrThrow(Enchantments.LOOTING),
                                    ContextFloatProviders.between(0f, 1f)
                                )
                            )
                    }
                }
            )
            // Drops Trader Catalog from Wandering Trader
            /*context.accept(
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
