package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.machine.HTMachineType
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import java.util.concurrent.CompletableFuture

class RagiumLootProvider(dataOutput: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricBlockLootTableProvider(dataOutput, registryLookup) {
    private fun <T : Any> getWrapperOrThrow(registryKey: RegistryKey<Registry<T>>): RegistryWrapper.Impl<T> =
        this.registryLookup.getWrapperOrThrow(registryKey)

    private val enchantmentRegistry: RegistryWrapper.Impl<Enchantment> by lazy { getWrapperOrThrow(RegistryKeys.ENCHANTMENT) }

    private fun getEnchant(key: RegistryKey<Enchantment>): RegistryEntry.Reference<Enchantment> = enchantmentRegistry.getOrThrow(key)

    private val fortune: RegistryEntry.Reference<Enchantment> by lazy { getEnchant(Enchantments.FORTUNE) }

    override fun generate() {
        addDrop(RagiumContents.RAGINITE_ORE, ::dropRaginiteOre)
        addDrop(RagiumContents.DEEPSLATE_RAGINITE_ORE, ::dropRaginiteOre)
        addDrop(RagiumContents.CREATIVE_SOURCE, dropsNothing())
        addDrop(RagiumContents.MANUAL_GRINDER)
        addDrop(RagiumContents.BURNING_BOX)
        addDrop(RagiumContents.WATER_GENERATOR)
        addDrop(RagiumContents.WIND_GENERATOR)
        addDrop(RagiumContents.SHAFT)
        addDrop(RagiumContents.GEAR_BOX)
        addDrop(RagiumContents.BLAZING_BOX)
        addDrop(RagiumContents.ALCHEMICAL_INFUSER)
        addDrop(RagiumContents.ITEM_DISPLAY)

        RagiumContents.StorageBlocks.entries
            .map(RagiumContents.StorageBlocks::block)
            .forEach(::addDrop)

        RagiumContents.Hulls.entries
            .map(RagiumContents.Hulls::block)
            .forEach(::addDrop)

        HTMachineType
            .getEntries()
            .map(HTMachineType::block)
            .forEach(::addDrop)

        RagiElement.entries.forEach { element: RagiElement ->
            // budding block
            addDrop(element.buddingBlock)
            // cluster block
            addDrop(element.clusterBlock) { block: Block ->
                dropsWithSilkTouch(
                    block,
                    ItemEntry
                        .builder(element.dustItem)
                        .applyDrop(4.0f)
                        .applyFortune()
                        .alternatively(
                            applyExplosionDecay(
                                block,
                                ItemEntry
                                    .builder(element.dustItem)
                                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))),
                            ),
                        ),
                )
            }
        }
    }

    private fun dropRaginiteOre(ore: Block): LootTable.Builder = dropsWithSilkTouch(
        ore,
        applyExplosionDecay(
            ore,
            ItemEntry
                .builder(RagiumItems.RAW_RAGINITE)
                .applyDropRange(2, 5)
                .applyFortune(),
        ),
    )

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDrop(value: Float): T =
        apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(value)))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDropRange(min: Number, max: Number): T =
        apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat())))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyFortune(): T = apply(ApplyBonusLootFunction.oreDrops(fortune))
}
