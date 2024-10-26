package hiiragi283.ragium.data

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
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
        addDrop(RagiumBlocks.SPONGE_CAKE)

        addDrop(RagiumBlocks.CREATIVE_SOURCE, dropsNothing())
        addDrop(RagiumBlocks.MANUAL_GRINDER)
        addDrop(RagiumBlocks.SHAFT)
        addDrop(RagiumBlocks.ITEM_DISPLAY)
        addDrop(RagiumBlocks.NETWORK_INTERFACE)
        addDrop(RagiumBlocks.BASIC_CASING)
        addDrop(RagiumBlocks.ADVANCED_CASING)
        addDrop(RagiumBlocks.POROUS_NETHERRACK) { block: Block -> withSilkTouch(block, Items.NETHERRACK) }

        addDrop(RagiumBlocks.META_MACHINE) { block: Block ->
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
                                            .include(HTMachineType.COMPONENT_TYPE)
                                            .include(HTMachineTier.COMPONENT_TYPE),
                                    ),
                            ),
                    ),
                )
        }

        RagiumContents.Ores.entries.forEach(::dropOre)

        // RagiumContents.Crops.entries.forEach(::addCrops)

        buildList {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
        }.map { it.value }.forEach(::addDrop)

        // HTMachineBlockRegistry.forEachBlock(::addDrop)

        /*RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            // budding block
            addDrop(element.buddingBlock)
            // cluster block
            addDrop(element.clusterBlock) { block: Block ->
                dropsWithSilkTouch(
                    block,
                    ItemEntry
                        .builder(element.dustItem)
                        .applyDrop(4.0f)
                        .applyFortune(),
                )
            }
        }*/
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

    /*private fun addCrops(crop: RagiumContents.Crops) {
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

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDrop(value: Float): T =
        apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(value)))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDropRange(min: Number, max: Number): T =
        apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat())))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyFortune(): T = apply(ApplyBonusLootFunction.oreDrops(fortune))
}
