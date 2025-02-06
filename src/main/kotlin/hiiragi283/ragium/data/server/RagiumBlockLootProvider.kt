package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SLAG_BLOCK)

            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            addAll(RagiumBlocks.GRATES.values)
            addAll(RagiumBlocks.BURNERS.values)
            add(RagiumBlocks.SHAFT)
            addAll(RagiumBlocks.GLASSES)

            add(RagiumBlocks.PLASTIC_BLOCK)
            addAll(RagiumBlocks.LED_BLOCKS.values)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            addAll(RagiumBlocks.ADDONS)

            addAll(RagiumAPI.machineRegistry.blocks)
        }.map(DeferredBlock<*>::get)
            .forEach(::dropSelf)

        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            val prefix: HTTagPrefix = when (key) {
                RagiumMaterials.RAGINITE -> HTTagPrefix.RAW_MATERIAL
                RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
                else -> return@forEach
            }
            val rawMaterial: ItemLike = RagiumItems.getMaterialItem(prefix, key)
            add(ore.get()) { block: Block -> createOreDrop(block, rawMaterial.asItem()) }
        }

        RagiumBlocks.DRUMS.forEach { (_, drum: DeferredBlock<HTDrumBlock>) ->
            add(drum.get()) { copyComponent(it, RagiumComponentTypes.FLUID_CONTENT) }
        }
    }

    private fun copyComponent(block: Block, type: Supplier<out DataComponentType<*>>): LootTable.Builder = LootTable
        .lootTable()
        .withPool(
            applyExplosionCondition(
                block,
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(
                        LootItem
                            .lootTableItem(block)
                            .apply(
                                CopyComponentsFunction
                                    .copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .include(type.get()),
                            ),
                    ),
            ),
        )

    override fun getKnownBlocks(): Iterable<Block> = RagiumBlocks.REGISTER.entries.map(DeferredHolder<Block, out Block>::get)
}
