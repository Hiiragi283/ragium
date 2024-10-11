package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.util.HTBlockContent
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
        addDrop(RagiumContents.SPONGE_CAKE)

        addDrop(RagiumContents.CREATIVE_SOURCE, dropsNothing())
        addDrop(RagiumContents.MANUAL_GRINDER)
        addDrop(RagiumContents.SHAFT)
        addDrop(RagiumContents.ALCHEMICAL_INFUSER)
        addDrop(RagiumContents.ITEM_DISPLAY)
        addDrop(RagiumContents.DATA_DRIVE)
        addDrop(RagiumContents.DRIVE_SCANNER)
        addDrop(RagiumContents.NETWORK_INTERFACE)
        addDrop(RagiumContents.BASIC_CASING)
        addDrop(RagiumContents.ADVANCED_CASING)
        addDrop(RagiumContents.POROUS_NETHERRACK) { block: Block -> withSilkTouch(block, Items.NETHERRACK) }

        addDrop(RagiumContents.META_MACHINE) { block: Block ->
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
                                            .include(RagiumComponentTypes.MACHINE_TYPE)
                                            .include(RagiumComponentTypes.MACHINE_TIER),
                                    ),
                            ),
                    ),
                )
        }

        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            addDrop(ore.block) { _: Block -> dropOre(ore, Items.COBBLESTONE) }
        }
        RagiumContents.DeepOres.entries.forEach { deepOre: RagiumContents.DeepOres ->
            addDrop(deepOre.block) { _: Block -> dropOre(deepOre, Items.COBBLED_DEEPSLATE) }
        }

        buildList<HTBlockContent> {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
        }.map(HTBlockContent::block).forEach(::addDrop)

        // HTMachineBlockRegistry.forEachBlock(::addDrop)

        RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
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
        }
    }

    private fun dropOre(ore: HTBlockContent, base: ItemConvertible): LootTable.Builder = dropsWithSilkTouch(
        ore.block,
        applyExplosionDecay(
            ore,
            ItemEntry
                .builder(ore.material.getRawMaterial() ?: base)
                .applyDropRange(2, 3)
                .applyFortune(),
        ),
    )

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
