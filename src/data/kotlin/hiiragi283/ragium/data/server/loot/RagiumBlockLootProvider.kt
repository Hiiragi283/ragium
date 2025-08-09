package hiiragi283.ragium.data.server.loot

import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.util.HTMaterialType
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTBuildingBlockSets
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
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.function.Supplier

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        RagiumBlocks.REGISTER.entries.forEach(::dropSelf)

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
        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.addBlockLoot()
        }

        // Log
        add(RagiumBlocks.ASH_LOG.get()) { block: Block ->
            applyExplosionDecay(
                block,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(LootItem.lootTableItem(RagiumItems.Dusts.ASH))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 3f)))
                            .apply(ApplyBonusCount.addUniformBonusCount(fortune)),
                    ),
            )
        }

        // Crop
        addCrop(RagiumBlocks.EXP_BERRY_BUSH, RagiumItems.EXP_BERRIES)
        addCrop(RagiumBlocks.WARPED_WART, RagiumItems.WARPED_WART)

        // Ore
        for (ore: HTBlockHolderLike.Materialized in RagiumBlocks.ORES) {
            val factory: (Block) -> LootTable.Builder = when (ore.material) {
                HTMaterialType.RAGINITE -> { block: Block ->
                    createSilkTouchDispatchTable(
                        block,
                        applyExplosionDecay(
                            block,
                            LootItem
                                .lootTableItem(RagiumItems.Dusts.RAGINITE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4f, 5f)))
                                .apply(ApplyBonusCount.addUniformBonusCount(fortune)),
                        ),
                    )
                }
                HTMaterialType.RAGI_CRYSTAL -> { block: Block ->
                    createOreDrop(
                        block,
                        RagiumItems.Gems.RAGI_CRYSTAL.get(),
                    )
                }
                else -> break
            }
            add(ore.get(), factory)
        }

        // Food
        /*add(RagiumBlocks.COOKED_MEAT_ON_THE_BONE.get()) { block: Block ->
            val propertyCondition: LootItemBlockStatePropertyCondition.Builder =
                LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(block)
                    .setProperties(
                        StatePropertiesPredicate.Builder
                            .properties()
                            .hasProperty(HTBlockStateProperties.MEAT_SERVINGS, 8)
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
        }*/

        // Storages
        for (holder: RagiumBlocks.Drums in RagiumBlocks.Drums.entries) {
            add(holder.get()) {
                copyComponent(
                    it,
                    DataComponents.ENCHANTMENTS,
                    RagiumDataComponents.FLUID_CONTENT.get(),
                )
            }
        }
    }

    private val blocks: MutableList<Block> = mutableListOf()

    override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
        blocks.add(block)
    }

    override fun getKnownBlocks(): Iterable<Block> = blocks

    //    Extensions    //

    private val fortune: Holder.Reference<Enchantment> get() = registries.enchLookup().getOrThrow(Enchantments.FORTUNE)

    private fun dropSelf(holder: Supplier<out Block>) {
        dropSelf(holder.get())
    }

    private fun addCrop(holder: Supplier<out Block>, crop: ItemLike) {
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
