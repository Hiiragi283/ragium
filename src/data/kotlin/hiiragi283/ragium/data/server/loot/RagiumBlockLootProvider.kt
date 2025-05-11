package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTOreSets
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.data.loot.BlockLootSubProvider
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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import net.neoforged.neoforge.registries.DeferredBlock

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        RagiumBlocks.REGISTER.entries.forEach(::dropSelf)

        add(RagiumBlocks.SPONGE_CAKE_SLAB.get(), ::createSlabItemTable)

        add(RagiumBlocks.SWEET_BERRIES_CAKE.get()) { block: Block ->
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem
                        .lootTableItem(RagiumItems.SWEET_BERRIES_CAKE_PIECE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(8f))),
                ),
            )
        }

        // Decorations
        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.addBlockLoot()
        }

        // Log
        fortuneDrop(
            RagiumBlocks.ASH_LOG,
            UniformGenerator.between(1f, 3f),
            RagiumItems.ASH_DUST,
        )

        // Bush
        add(RagiumBlocks.EXP_BERRY_BUSH.get()) { block: Block ->
            createSingleItemTableWithSilkTouch(
                block,
                RagiumItems.EXP_BERRIES,
            )
        }

        // Ore
        fun registerOres(oreSets: HTOreSets, drop: ItemLike) {
            for (ore: DeferredBlock<*> in oreSets.blockHolders) {
                add(ore.get()) { block: Block ->
                    createSilkTouchDispatchTable(
                        block,
                        applyExplosionDecay(
                            block,
                            LootItem
                                .lootTableItem(drop.asItem())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4f, 5f)))
                                .apply(
                                    ApplyBonusCount.addUniformBonusCount(
                                        registries.enchLookup().getOrThrow(Enchantments.FORTUNE),
                                    ),
                                ),
                        ),
                    )
                    // createOreDrop(block, drop.asItem())
                }
            }
        }
        registerOres(RagiumBlocks.RAGINITE_ORES, RagiumItems.RAGINITE_DUST)

        // Machines
        for (holder: DeferredBlock<*> in RagiumBlocks.MACHINES) {
            add(holder.get()) { copyComponent(it, DataComponents.ENCHANTMENTS) }
        }

        // Food
        add(RagiumBlocks.COOKED_MEAT_ON_THE_BONE.get()) { block: Block ->
            val propertyCondition: LootItemBlockStatePropertyCondition.Builder =
                LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(block)
                    .setProperties(
                        StatePropertiesPredicate.Builder
                            .properties()
                            .hasProperty(HTBlockStateProperties.MEAT_SERVINGS, 8),
                    )

            LootTable
                .lootTable()
                // 一度もかけていない場合はそのままドロップ
                .withPool(
                    LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .`when`(propertyCondition)
                        .add(LootItem.lootTableItem(block)),
                )
                // 一度でもかけていたら骨をドロップ
                .withPool(
                    LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .`when`(propertyCondition.invert())
                        .add(LootItem.lootTableItem(Items.BONE)),
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

    private fun getFortune(): Holder.Reference<Enchantment> = registries.enchLookup().getOrThrow(Enchantments.FORTUNE)

    private fun dropSelf(holder: DeferredBlock<*>) {
        dropSelf(holder.get())
    }

    private fun fortuneDrop(holder: DeferredBlock<*>, range: NumberProvider, drop: ItemLike = holder) {
        add(holder.get()) { block: Block ->
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem
                        .lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(range))
                        .apply(ApplyBonusCount.addOreBonusCount(getFortune())),
                ),
            )
        }
    }

    private fun copyComponent(block: Block, vararg types: DataComponentType<*>): LootTable.Builder = LootTable
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
                                    .apply { types.forEach(this::include) },
                            ),
                    ),
            ),
        )

    private fun HTBuildingBlockSets.addBlockLoot() {
        // Base
        dropSelf(this.base)
        // Stairs
        dropSelf(this.stairs)
        // Slab
        add(this.slab.get(), ::createSlabItemTable)
        // Wall
        dropSelf(this.wall)
    }
}
