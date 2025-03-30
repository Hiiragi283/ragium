package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import hiiragi283.ragium.common.util.HTOreSets
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
        buildList {
            add(RagiumBlocks.RAGI_BRICK_SETS)
            add(RagiumBlocks.AZURE_TILE_SETS)
            add(RagiumBlocks.EMBER_STONE_SETS)
            add(RagiumBlocks.PLASTIC_SETS)
            add(RagiumBlocks.BLUE_NETHER_BRICK_SETS)
        }.forEach { it.addBlockLoot() }

        // Log
        fortuneDrop(
            RagiumBlocks.ASH_LOG,
            UniformGenerator.between(1f, 3f),
            RagiumItems.Dusts.ASH,
        )

        // Bush
        add(RagiumBlocks.EXP_BERRY_BUSH.get()) { block: Block ->
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem
                        .lootTableItem(RagiumItems.EXP_BERRIES)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f)))
                        .apply(ApplyBonusCount.addUniformBonusCount(getFortune())),
                ),
            )
        }

        // Ore
        fun registerOres(oreSets: HTOreSets, drop: ItemLike) {
            for (ore: DeferredBlock<*> in oreSets.blockHolders) {
                add(ore.get()) { block: Block -> createOreDrop(block, drop.asItem()) }
            }
        }
        registerOres(RagiumBlocks.RAGINITE_ORES, RagiumItems.RawResources.RAGINITE)
        registerOres(RagiumBlocks.RAGI_CRYSTAL_ORES, RagiumItems.RawResources.RAGI_CRYSTAL)

        add(RagiumBlocks.STICKY_SOUL_SOIL.get()) { block: Block ->
            createOreDrop(block, RagiumItems.TAR.get())
        }

        // Machines
        for (holder: DeferredBlock<*> in RagiumBlocks.MACHINES) {
            add(holder.get()) { copyComponent(it, DataComponents.ENCHANTMENTS) }
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
