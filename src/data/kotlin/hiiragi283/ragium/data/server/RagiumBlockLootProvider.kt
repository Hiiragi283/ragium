package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.util.HTOreSets
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
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
import java.util.function.Supplier

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            remove(RagiumBlocks.CRUDE_OIL)
            remove(RagiumBlocks.ASH_LOG)

            // removeAll(RagiumBlocks.CRATES.values)
            removeAll(RagiumBlocks.DRUMS.values)
        }.map(Supplier<out Block>::get).forEach(::dropSelf)

        fortuneDrop(
            RagiumBlocks.ASH_LOG,
            UniformGenerator.between(1f, 3f),
            RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ASH),
        )

        fun registerOres(oreSets: HTOreSets, prefix: HTTagPrefix) {
            for (ore: DeferredBlock<out Block> in oreSets.oreBlocks) {
                val rawMaterial: ItemLike = RagiumItems.getMaterialItem(prefix, oreSets.key)
                fortuneDrop(ore, UniformGenerator.between(1f, 3f), rawMaterial)
            }
        }
        registerOres(RagiumBlocks.RAGINITE_ORES, HTTagPrefix.RAW_MATERIAL)
        registerOres(RagiumBlocks.RAGI_CRYSTAL_ORES, HTTagPrefix.GEM)

        for (drum: DeferredBlock<HTDrumBlock> in RagiumBlocks.DRUMS.values) {
            add(drum.get()) {
                copyComponent(
                    it,
                    RagiumComponentTypes.FLUID_CONTENT.get(),
                    DataComponents.ENCHANTMENTS,
                )
            }
        }

        for (holder: DeferredBlock<*> in HTMachineType.getBlocks()) {
            add(holder.get()) { copyComponent(it, DataComponents.ENCHANTMENTS) }
        }
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
                        .apply(
                            ApplyBonusCount.addOreBonusCount(
                                registries.enchLookup().getOrThrow(Enchantments.FORTUNE),
                            ),
                        ),
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

    private val blocks: MutableList<Block> = mutableListOf()

    override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
        blocks.add(block)
    }

    override fun getKnownBlocks(): Iterable<Block> = blocks
}
