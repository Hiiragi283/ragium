package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlock
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {
    private val fortune: Holder<Enchantment> = registries.holderOrThrow(Enchantments.FORTUNE)

    override fun generate() {
        RagiumBlocks.REGISTER
            .blockEntries
            .asSequence()
            .map(HTDeferredOnlyBlock<*>::get)
            .forEach { block: Block ->
                add(
                    block,
                    if (block is HTBlockWithEntity) {
                        copyComponent(block) {
                            include(DataComponents.CUSTOM_NAME)
                            include(DataComponents.ENCHANTMENTS)
                            include(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                            when (block) {
                                is HTDrumBlock -> include(RagiumDataComponents.FLUID_CONTENT)
                                is HTCrateBlock -> include(RagiumDataComponents.ITEM_CONTENT)
                            }
                        }
                    } else {
                        createSingleItemTable(block)
                    },
                )
            }

        add(RagiumBlocks.SWEET_BERRIES_CAKE.get()) { block: Block ->
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem
                        .lootTableItem(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(7f))),
                ),
            )
        }

        // Decorations
        for (slab: HTDeferredBlock<*, *> in RagiumBlocks.SLABS.values) {
            add(slab.get(), ::createSlabItemTable)
        }

        // Crop
        addCrop(RagiumBlocks.EXP_BERRIES, RagiumBlocks.EXP_BERRIES)
        addCrop(RagiumBlocks.WARPED_WART, RagiumBlocks.WARPED_WART)

        // Ore
        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: HTDeferredBlock<*, *>) ->
            val factory: (Block) -> LootTable.Builder = when (key) {
                RagiumMaterialKeys.RAGINITE -> { block: Block ->
                    createSilkTouchDispatchTable(
                        block,
                        applyExplosionDecay(
                            block,
                            LootItem
                                .lootTableItem(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4f, 5f)))
                                .apply(ApplyBonusCount.addUniformBonusCount(fortune)),
                        ),
                    )
                }

                else -> { block: Block -> createOreDrop(block, RagiumItems.getGem(key).get()) }
            }
            add(ore.get(), factory)
        }

        add(RagiumBlocks.BUDDING_QUARTZ.get(), noDrop())
        add(RagiumBlocks.QUARTZ_CLUSTER.get()) { block: Block ->
            createSilkTouchDispatchTable(
                block,
                LootItem
                    .lootTableItem(Items.QUARTZ)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4f)))
                    .apply(ApplyBonusCount.addOreBonusCount(fortune))
                    .`when`(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                    .otherwise(
                        applyExplosionDecay(
                            block,
                            LootItem
                                .lootTableItem(Items.QUARTZ)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2f))),
                        ),
                    ),
            )
        }
    }

    private val blocks: MutableList<Block> = mutableListOf()

    override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
        blocks.add(block)
    }

    override fun getKnownBlocks(): Iterable<Block> = blocks

    //    Extensions    //

    private fun addCrop(holder: HTDeferredBlock<*, *>, crop: ItemLike) {
        add(holder.get()) { block: Block ->
            applyExplosionDecay(
                block,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .`when`(
                                LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(
                                        StatePropertiesPredicate.Builder.properties().hasProperty(HTCropBlock.AGE, 3),
                                    ),
                            ).add(LootItem.lootTableItem(crop))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 3f)))
                            .apply(ApplyBonusCount.addUniformBonusCount(fortune)),
                    ).withPool(
                        LootPool
                            .lootPool()
                            .`when`(
                                LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(
                                        StatePropertiesPredicate.Builder.properties().hasProperty(HTCropBlock.AGE, 2),
                                    ),
                            ).add(LootItem.lootTableItem(crop))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 2f)))
                            .apply(ApplyBonusCount.addUniformBonusCount(fortune)),
                    ),
            )
        }
    }

    private fun copyComponent(block: Block, builderAction: CopyComponentsFunction.Builder.() -> Unit): LootTable.Builder = LootTable
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
                                    .apply(builderAction),
                            ),
                    ),
            ),
        )
}
