package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Coils.entries)
            addAll(RagiumBlocks.Burners.entries)
            add(RagiumBlocks.SHAFT)
            add(RagiumBlocks.CHEMICAL_GLASS)

            add(RagiumBlocks.PLASTIC_BLOCK)
            addAll(RagiumBlocks.Decorations.entries)
            addAll(RagiumBlocks.LEDBlocks.entries)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)

            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)

            addAll(RagiumAPI.machineRegistry.blockMap.values)
        }.map(Supplier<out Block>::get)
            .forEach(::dropSelf)

        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            val rawMaterial: ItemLike = when (ore.material) {
                RagiumMaterialKeys.CRUDE_RAGINITE -> RagiumItems.RawResources.RAW_CRUDE_RAGINITE
                RagiumMaterialKeys.RAGINITE -> RagiumItems.RawResources.RAW_RAGINITE
                RagiumMaterialKeys.RAGI_CRYSTAL -> RagiumItems.RawResources.RAGI_CRYSTAL
                else -> return@forEach
            }

            add(ore.get()) { block: Block -> createOreDrop(block, rawMaterial.asItem()) }
        }

        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            add(drum.get()) { copyComponent(it, RagiumComponentTypes.FLUID_CONTENT) }
        }
    }

    private fun copyComponent(block: Block, type: Supplier<out DataComponentType<*>>): LootTable.Builder = LootTable
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
                                    .include(type.get()),
                            ),
                    ),
            ),
        )

    override fun getKnownBlocks(): Iterable<Block> = RagiumBlocks.REGISTER.entries.map(DeferredHolder<Block, out Block>::get)
}
