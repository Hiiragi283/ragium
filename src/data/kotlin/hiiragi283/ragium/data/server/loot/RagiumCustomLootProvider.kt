package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.*
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.loot.CanItemPerformAbility
import java.util.function.BiConsumer

class RagiumCustomLootProvider(provider: HolderLookup.Provider) : LootTableSubProvider {
    companion object {
        @JvmField
        val DROP_RAGI_CHERRY: ResourceKey<LootTable> =
            ResourceKey.create(Registries.LOOT_TABLE, RagiumAPI.id("drop_ragi_cherry"))
    }

    private val enchLookup: HolderLookup.RegistryLookup<Enchantment> = provider.enchLookup()
    private val hasShears: LootItemCondition.Builder =
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)

    private fun hasSilkTouch(): LootItemCondition.Builder = MatchTool.toolMatches(
        ItemPredicate.Builder
            .item()
            .withSubPredicate(
                ItemSubPredicates.ENCHANTMENTS,
                ItemEnchantmentsPredicate.enchantments(
                    listOf(
                        EnchantmentPredicate(
                            enchLookup.getOrThrow(Enchantments.SILK_TOUCH),
                            MinMaxBounds.Ints.atLeast(1),
                        ),
                    ),
                ),
            ),
    )

    private fun hasShearOrSilkTouch(): LootItemCondition.Builder = hasShears.or(hasSilkTouch())

    private fun doesNotHaveShearsOrSilkTouch(): LootItemCondition.Builder = hasShearOrSilkTouch().invert()

    /**
     * @see [net.minecraft.data.loot.packs.VanillaBlockLoot.createOakLeavesDrops]
     */
    override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
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
                                        enchLookup.getOrThrow(Enchantments.FORTUNE),
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
