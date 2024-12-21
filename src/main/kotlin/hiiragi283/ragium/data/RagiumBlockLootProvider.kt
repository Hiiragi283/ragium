package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.RagiumContents.RawMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.component.ComponentType
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

    private fun addDrop(content: HTBlockContent) {
        addDrop(content.get())
    }

    private fun addDrop(content: HTBlockContent, builder: (Block) -> LootTable.Builder) {
        addDrop(content.get(), builder)
    }

    private fun addDrop(content: HTBlockContent, builder: LootTable.Builder) {
        addDrop(content.get(), builder)
    }

    override fun generate() {
        addDrop(RagiumBlocks.CREATIVE_CRATE)
        addDrop(RagiumBlocks.CREATIVE_DRUM)
        addDrop(RagiumBlocks.CREATIVE_EXPORTER)
        addDrop(RagiumBlocks.CREATIVE_SOURCE)

        addDrop(RagiumBlocks.MUTATED_SOIL)
        addDrop(RagiumBlocks.POROUS_NETHERRACK) { block: Block -> withSilkTouch(block, Items.NETHERRACK) }

        RagiumBlocks.Stones.entries.forEach(::addDrop)
        RagiumBlocks.Slabs.entries.forEach { addDrop(it, ::slabDrops) }
        RagiumBlocks.Stairs.entries.forEach(::addDrop)
        RagiumBlocks.BUILDINGS.forEach(::addDrop)

        addDrop(RagiumBlocks.SPONGE_CAKE)
        addDrop(
            RagiumBlocks.SWEET_BERRIES_CAKE,
            drops(
                RagiumBlocks.SWEET_BERRIES_CAKE.get(),
                RagiumItems.SWEET_BERRIES_CAKE_PIECE,
                ConstantLootNumberProvider.create(8f),
            ),
        )

        RagiumContents.Ores.entries.forEach(::dropOre)

        buildList {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Grates.entries)
            addAll(RagiumContents.Casings.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
            addAll(RagiumContents.Exporters.entries)
            addAll(RagiumContents.Pipes.entries)
            addAll(RagiumContents.CrossPipes.entries)
            addAll(RagiumContents.PipeStations.entries)
            addAll(RagiumContents.FilteringPipe.entries)

            addAll(RagiumBlocks.MECHANICS)
            addAll(RagiumBlocks.MISC)
        }.forEach(::addDrop)

        RagiumContents.Crates.entries
            .map(RagiumContents.Crates::get)
            .forEach { dropWithComponent(it, RagiumComponentTypes.CRATE) }
        RagiumContents.Drums.entries
            .map(RagiumContents.Drums::get)
            .forEach { dropWithComponent(it, RagiumComponentTypes.DRUM) }

        RagiumAPI
            .getInstance()
            .machineRegistry
            .blocks
            .forEach { block: HTMachineBlock -> dropWithComponent(block, HTMachineTier.COMPONENT_TYPE) }
    }

    private fun dropOre(ore: RagiumContents.Ores) {
        val dropMineral: ItemConvertible = when (ore) {
            RagiumContents.Ores.CRUDE_RAGINITE -> RawMaterials.CRUDE_RAGINITE
            RagiumContents.Ores.DEEP_RAGINITE -> RawMaterials.RAGINITE
            RagiumContents.Ores.NETHER_RAGINITE -> RawMaterials.RAGINITE
            RagiumContents.Ores.END_RAGI_CRYSTAL -> RagiumContents.Gems.RAGI_CRYSTAL
        }

        addDrop(
            ore.get(),
            dropsWithSilkTouch(
                ore.get(),
                applyExplosionDecay(
                    ore,
                    ItemEntry
                        .builder(dropMineral)
                        .applyDropRange(1, 3)
                        .applyFortune(),
                ),
            ),
        )
    }

    private fun dropWithComponent(block: Block, vararg types: ComponentType<*>) {
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
                                            .apply { types.forEach(this::include) },
                                    ),
                            ),
                    ),
                )
        }
    }

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
