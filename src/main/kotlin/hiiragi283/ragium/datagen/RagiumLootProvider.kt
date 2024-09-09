package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
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

class RagiumLootProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : FabricBlockLootTableProvider(dataOutput, registryLookup) {

    private val enchantmentRegistry: RegistryWrapper.Impl<Enchantment> by lazy {
        getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
    }

    private fun <T : Any> getWrapperOrThrow(registryKey: RegistryKey<Registry<T>>): RegistryWrapper.Impl<T> =
        this.registryLookup.getWrapperOrThrow(registryKey)

    override fun generate() {
        // Raginite Ore
        dropRaginiteOre(RagiumBlocks.RAGINITE_ORE)
        dropRaginiteOre(RagiumBlocks.DEEPSLATE_RAGINITE_ORE)
    }

    private fun dropRaginiteOre(ore: Block) {
        dropsWithSilkTouch(
            ore,
            applyExplosionDecay(
                RagiumBlocks.RAGINITE_ORE,
                ItemEntry.builder(RagiumItems.RAW_RAGINITE)
                    .applyDropRange(2, 5)
                    .applyFortune()
            )
        )
    }

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyDropRange(min: Number, max: Number): T =
        apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat())))

    fun <T : LeafEntry.Builder<T>> LeafEntry.Builder<T>.applyFortune(): T =
        apply(ApplyBonusLootFunction.oreDrops(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE)))

}