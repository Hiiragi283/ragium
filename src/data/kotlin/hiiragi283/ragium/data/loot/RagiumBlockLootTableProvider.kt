package hiiragi283.ragium.data.loot

import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.lib.data.loot.builder.HTLootTableBuilder
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition

class RagiumBlockLootTableProvider(registries: HolderLookup.Provider) :
    HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        knownBlocks.forEach(::dropSelf)

        RagiumBlocks.STORAGE_BLOCKS.values.forEach(::dropSelf)

        RagiumBlocks.ALL_DECORATIONS.forEach { add(it.slab, ::createSlabItemTable) }
        // Machine
        for (machine: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINES.values.flatten()) {
            add(machine, ::copyComponent)
        }
        // Storage
        listOf(
            RagiumBlocks.FLUID_OUTPUT_BUS,
            RagiumBlocks.TANK,
            RagiumBlocks.POTION_TANK,
            RagiumBlocks.CREATIVE_TANK
        ).forEach { blockItem: HTSimpleDeferredBlockAndItem ->
            add(blockItem) { block: Block -> copyComponent(block) { include(RagiumDataComponents.FLUID) } }
        }
    }

    private inline fun buildTable(builderAction: HTLootTableBuilder.() -> Unit): LootTable.Builder =
        HTLootTableBuilder.build(builderAction)

    private inline fun copyComponent(
        block: Block,
        builderAction: CopyComponentsFunction.Builder.() -> Unit = {}
    ): LootTable.Builder = buildTable {
        pool {
            +LootItem
                .lootTableItem(block)
                .apply(
                    CopyComponentsFunction
                        .copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                        .include(DataComponents.CUSTOM_NAME)
                        .include(RagiumDataComponents.OWNER_ID)
                        .apply(builderAction)
                )
            +ExplosionCondition.survivesExplosion()
        }
    }
}
