package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTGlobalLootModifierProvider
import hiiragi283.lib.data.loot.predicates.HTBlockSetLootCondition
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.BlockItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.concurrent.CompletableFuture

class RagiumGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    HTGlobalLootModifierProvider(output, registries, RagiumAPI.MOD_ID) {
    override fun start() {
        add(RagiumGlobalLootTables.SULFUR_FROM_COAL_ORES, block(BlockItemTags.COAL_ORES))
        add(RagiumGlobalLootTables.CINNABAR_FROM_REDSTONE_ORES, block(BlockItemTags.REDSTONE_ORES))
        add(RagiumGlobalLootTables.FLUORITE_FROM_LAPIS_ORES, block(BlockItemTags.LAPIS_ORES))
        add(RagiumGlobalLootTables.CARBON_FROM_DIAMOND_ORES, block(BlockItemTags.DIAMOND_ORES))

        add(RagiumGlobalLootTables.ELDER_HEART, condition(EntityTypes.ELDER_GUARDIAN))
        add(RagiumGlobalLootTables.TRADER_CATALOG, condition(EntityTypes.WANDERING_TRADER))
    }

    private fun block(tagKey: BlockItemTagId): LootItemCondition = block(tagKey.block())

    private fun block(tagKey: TagKey<Block>): LootItemCondition = HTBlockSetLootCondition(registries.getOrThrow(tagKey))
}
