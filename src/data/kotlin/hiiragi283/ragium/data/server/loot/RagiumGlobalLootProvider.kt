package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition
import java.util.concurrent.CompletableFuture

class RagiumGlobalLootProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    GlobalLootModifierProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        // Drops Ragi-Cherry from Cherry Leaves
        add(RagiumCustomLootProvider.DROP_RAGI_CHERRY, builder(Blocks.CHERRY_LEAVES).build())
        // Drops Elder Heart from Elder Guardian
        add(RagiumCustomLootProvider.DROP_ELDER_HEART, builder(EntityType.ELDER_GUARDIAN).build())
        // Drops Trader Catalog from Wandering Trader
        add(RagiumCustomLootProvider.DROP_TRADER_CATALOG, builder(EntityType.WANDERING_TRADER).build())
    }

    private fun add(key: ResourceKey<LootTable>, vararg conditions: LootItemCondition) {
        add(key.location().path, AddTableLootModifier(arrayOf(*conditions), key))
    }

    private fun builder(block: Block): LootTableIdCondition.Builder = LootTableIdCondition.builder(block.lootTable.location())

    private fun builder(entityType: EntityType<*>): LootTableIdCondition.Builder =
        LootTableIdCondition.builder(entityType.defaultLootTable.location())
}
