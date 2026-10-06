package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTGlobalLootModifierProvider
import hiiragi283.lib.data.loot.predicates.HTBlockSetLootCondition
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.concurrent.CompletableFuture

class RagiumGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    HTGlobalLootModifierProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        add(RagiumGlobalLootTables.SULFUR_FROM_COAL_ORES, block(BlockTags.COAL_ORES))
        add(RagiumGlobalLootTables.CINNABAR_FROM_REDSTONE_ORES, block(BlockTags.REDSTONE_ORES))
        add(RagiumGlobalLootTables.FLUORITE_FROM_LAPIS_ORES, block(BlockTags.LAPIS_ORES))
        add(RagiumGlobalLootTables.CARBON_FROM_DIAMOND_ORES, block(BlockTags.DIAMOND_ORES))

        add(RagiumGlobalLootTables.ELDER_HEART, condition(EntityType.ELDER_GUARDIAN))
        add(RagiumGlobalLootTables.TRADER_CATALOG, condition(EntityType.WANDERING_TRADER))
    }

    private fun block(tagKey: TagKey<Block>): LootItemCondition = HTBlockSetLootCondition(registries.getOrThrow(tagKey))
}
