package hiiragi283.ragium.data

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
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
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RagiumLootProvider(dataOutput: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricBlockLootTableProvider(dataOutput, registryLookup) {
    private val enchantmentRegistry: RegistryWrapper.Impl<Enchantment> by lazy {
        getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
    }

    private fun <T : Any> getWrapperOrThrow(registryKey: RegistryKey<Registry<T>>): RegistryWrapper.Impl<T> =
        this.registryLookup.getWrapperOrThrow(registryKey)

    override fun generate() {
        RagiumBlocks.REGISTER.generateLootTable(this)

        addDrop(RagiumBlocks.RAGINITE_ORE, ::dropRaginiteOre)
        addDrop(RagiumBlocks.DEEPSLATE_RAGINITE_ORE, ::dropRaginiteOre)
        // addDrop(RagiumBlocks.CREATIVE_SOURCE)
        // tier1
        // addDrop(RagiumBlocks.RAGI_ALLOY_BLOCK)
        // addDrop(RagiumBlocks.RAGI_ALLOY_HULL)
        // addDrop(RagiumBlocks.MANUAL_GRINDER)
        // addDrop(RagiumBlocks.WATER_COLLECTOR)
        // addDrop(RagiumBlocks.BURNING_BOX)
        // tier2
        // addDrop(RagiumBlocks.RAGI_STEEL_BLOCK)
        // addDrop(RagiumBlocks.RAGI_STEEL_HULL)
        // tier3
        // addDrop(RagiumBlocks.REFINED_RAGI_STEEL_BLOCK)
        // addDrop(RagiumBlocks.REFINED_RAGI_STEEL_HULL)
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

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDropRange(min: Number, max: Number): T =
        apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat())))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyFortune(): T =
        apply(ApplyBonusLootFunction.oreDrops(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE)))
}
