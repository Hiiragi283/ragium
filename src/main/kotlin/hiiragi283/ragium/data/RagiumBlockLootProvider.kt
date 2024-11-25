package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.CopyComponentsLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import java.util.concurrent.CompletableFuture

class RagiumBlockLootProvider(dataOutput: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricBlockLootTableProvider(dataOutput, registryLookup) {
    private fun <T : Any> getWrapperOrThrow(registryKey: RegistryKey<Registry<T>>): RegistryWrapper.Impl<T> =
        this.registryLookup.getWrapperOrThrow(registryKey)

    private val enchantmentRegistry: RegistryWrapper.Impl<Enchantment> by lazy { getWrapperOrThrow(RegistryKeys.ENCHANTMENT) }

    private fun getEnchant(key: RegistryKey<Enchantment>): RegistryEntry.Reference<Enchantment> = enchantmentRegistry.getOrThrow(key)

    private val fortune: RegistryEntry.Reference<Enchantment> by lazy { getEnchant(Enchantments.FORTUNE) }

    override fun generate() {
        addDrop(RagiumBlocks.MUTATED_SOIL)
        addDrop(RagiumBlocks.POROUS_NETHERRACK) { block: Block -> withSilkTouch(block, Items.NETHERRACK) }

        // asphalt
        addDrop(RagiumBlocks.ASPHALT)
        addDrop(RagiumBlocks.ASPHALT_SLAB, ::slabDrops)
        addDrop(RagiumBlocks.ASPHALT_STAIRS)
        // addDrop(RagiumBlocks.LINED_ASPHALT)
        // addDrop(RagiumBlocks.LINED_ASPHALT_SLAB, ::slabDrops)
        // addDrop(RagiumBlocks.LINED_ASPHALT_STAIRS)
        addDrop(RagiumBlocks.POLISHED_ASPHALT)
        addDrop(RagiumBlocks.POLISHED_ASPHALT_SLAB, ::slabDrops)
        addDrop(RagiumBlocks.POLISHED_ASPHALT_STAIRS)
        // gypsum
        addDrop(RagiumBlocks.GYPSUM)
        addDrop(RagiumBlocks.GYPSUM_SLAB, ::slabDrops)
        addDrop(RagiumBlocks.GYPSUM_STAIRS)
        addDrop(RagiumBlocks.POLISHED_GYPSUM)
        addDrop(RagiumBlocks.POLISHED_GYPSUM_SLAB, ::slabDrops)
        addDrop(RagiumBlocks.POLISHED_GYPSUM_STAIRS)

        addDrop(RagiumBlocks.SPONGE_CAKE)
        addDrop(
            RagiumBlocks.SWEET_BERRIES_CAKE,
            drops(
                RagiumBlocks.SWEET_BERRIES_CAKE,
                RagiumItems.SWEET_BERRIES_CAKE_PIECE,
                ConstantLootNumberProvider.create(8f),
            ),
        )

        addDrop(RagiumBlocks.AUTO_ILLUMINATOR)
        addDrop(RagiumBlocks.CREATIVE_SOURCE, dropsNothing())
        addDrop(RagiumBlocks.LARGE_PROCESSOR)
        addDrop(RagiumBlocks.MANUAL_FORGE)
        addDrop(RagiumBlocks.MANUAL_GRINDER)
        addDrop(RagiumBlocks.MANUAL_MIXER)
        addDrop(RagiumBlocks.NETWORK_INTERFACE)
        addDrop(RagiumBlocks.OPEN_CRATE)
        addDrop(RagiumBlocks.TELEPORT_ANCHOR)
        addDrop(RagiumBlocks.TRASH_BOX)

        RagiumBlocks.MISC.forEach(::addDrop)

        RagiumContents.Ores.entries.forEach(::dropOre)

        buildList {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Grates.entries)
            addAll(RagiumContents.Casings.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
            addAll(RagiumContents.Exporters.entries)
            addAll(RagiumContents.Pipes.entries)
        }.map { it.value }.forEach(::addDrop)

        RagiumContents.Drums.entries.forEach { drum: RagiumContents.Drums ->
            addDrop(drum.value) { block: Block ->
                LootTable
                    .builder()
                    .pool(
                        addSurvivesExplosionCondition(
                            block,
                            LootPool
                                .builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .with(
                                    ItemEntry
                                        .builder(block)
                                        .apply(
                                            CopyComponentsLootFunction
                                                .builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                                                .include(RagiumComponentTypes.DRUM),
                                        ),
                                ),
                        ),
                    )
            }
        }

        RagiumAPI
            .getInstance()
            .machineRegistry.blocks
            .forEach(::addDrop)
    }

    private fun dropOre(ore: RagiumContents.Ores) {
        addDrop(
            ore.value,
            dropsWithSilkTouch(
                ore.value,
                applyExplosionDecay(
                    ore,
                    ItemEntry
                        .builder(ore.dropMineral)
                        .applyDropRange(1, 3)
                        .applyFortune(),
                ),
            ),
        )
    }

    /*private fun dropMachine(block: Block) {
        addDrop(block) { block1: Block ->
            LootTable
                .builder()
                .pool(
                    addSurvivesExplosionCondition(
                        block1,
                        LootPool
                            .builder()
                            .rolls(ConstantLootNumberProvider.create(1.0f))
                            .with(
                                ItemEntry
                                    .builder(block1)
                                    .apply(
                                        CopyComponentsLootFunction
                                            .builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                                            .include(HTMachineType.COMPONENT_TYPE)
                                            .include(HTMachineTier.COMPONENT_TYPE),
                                    ),
                            ),
                    ),
                )
        }
    }

    private fun addCrops(crop: RagiumContents.Crops) {
        val condition: BlockStatePropertyLootCondition.Builder = BlockStatePropertyLootCondition
            .builder(crop.cropBlock)
            .properties(StatePredicate.Builder.create().exactMatch(Properties.AGE_7, 7))
        val dropBuilder: LeafEntry.Builder<*> = ItemEntry.builder(crop.seedItem)
        addDrop(
            crop.cropBlock,
            applyExplosionDecay(
                crop.cropBlock,
                LootTable
                    .builder()
                    .pool(LootPool.builder().with(dropBuilder))
                    .pool(
                        LootPool
                            .builder()
                            .conditionally(condition)
                            .with(dropBuilder)
                            .apply(
                                ApplyBonusLootFunction.binomialWithBonusCount(fortune, 0.5714286f, 3),
                            ),
                    ),
            ),
        )
    }*/

    private fun withSilkTouch(with: Block, without: ItemConvertible, amount: Float = 1.0f): LootTable.Builder = dropsWithSilkTouch(
        with,
        applyExplosionDecay(
            with,
            ItemEntry
                .builder(without)
                .applyDrop(amount)
                .applyFortune(),
        ),
    )

    private fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDrop(value: Float): T =
        apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(value)))

    private fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDropRange(min: Number, max: Number): T =
        apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat())))

    private fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyFortune(): T = apply(ApplyBonusLootFunction.oreDrops(fortune))
}
