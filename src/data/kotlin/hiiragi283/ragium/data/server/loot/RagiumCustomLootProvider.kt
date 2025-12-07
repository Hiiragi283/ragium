package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.ItemSubPredicates
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.loot.CanItemPerformAbility
import java.util.function.BiConsumer

sealed class RagiumCustomLootProvider(protected val provider: HolderLookup.Provider) : LootTableSubProvider {
    companion object {
        @JvmField
        val DROP_RAGI_CHERRY: ResourceKey<LootTable> = create("drop_ragi_cherry")
        
        @JvmField
        val DROP_ELDER_HEART: ResourceKey<LootTable> = create("drop_elder_heart")

        @JvmField
        val DROP_TRADER_CATALOG: ResourceKey<LootTable> = create("drop_trader_catalog")

        @JvmStatic
        private fun create(path: String): ResourceKey<LootTable> = Registries.LOOT_TABLE.createKey(RagiumAPI.id(path))
    }

    protected val hasShears: LootItemCondition.Builder =
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)

    protected fun hasSilkTouch(): LootItemCondition.Builder = MatchTool.toolMatches(
        ItemPredicate.Builder
            .item()
            .withSubPredicate(
                ItemSubPredicates.ENCHANTMENTS,
                ItemEnchantmentsPredicate.enchantments(
                    listOf(
                        EnchantmentPredicate(
                            provider.holderOrThrow(Enchantments.SILK_TOUCH),
                            MinMaxBounds.Ints.atLeast(1),
                        ),
                    ),
                ),
            ),
    )

    protected fun hasShearOrSilkTouch(): LootItemCondition.Builder = hasShears.or(hasSilkTouch())

    protected fun doesNotHaveShearsOrSilkTouch(): LootItemCondition.Builder = hasShearOrSilkTouch().invert()

    //    Block    //

    class Block(provider: HolderLookup.Provider) : RagiumCustomLootProvider(provider) {
        /**
         * @see net.minecraft.data.loot.packs.VanillaBlockLoot.createOakLeavesDrops
         */
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Ragi-Cherry from Cherry Leaves
            output.accept(
                DROP_RAGI_CHERRY,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .`when`(doesNotHaveShearsOrSilkTouch())
                            .add(
                                LootItem
                                    .lootTableItem(RagiumItems.RAGI_CHERRY)
                                    .`when`(
                                        BonusLevelTableCondition.bonusLevelFlatChance(
                                            provider.holderOrThrow(Enchantments.FORTUNE),
                                            0.005f,
                                            0.0055f,
                                            0.00625f,
                                            0.00833f,
                                            0.025f,
                                        ),
                                    ),
                            ),
                    ),
            )
        }
    }

    //    Entity    //

    class Entity(provider: HolderLookup.Provider) : RagiumCustomLootProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Elder Heart from Elder Guardian
            output.accept(
                DROP_ELDER_HEART,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
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
            output.accept(
                DROP_TRADER_CATALOG,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .add(LootItem.lootTableItem(RagiumItems.TRADER_CATALOG)),
                    ),
            )
        }
    }
}
