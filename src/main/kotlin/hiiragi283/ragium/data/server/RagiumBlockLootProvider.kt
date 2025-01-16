package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.id
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
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
import java.util.function.Supplier

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Coils.entries)
            add(RagiumBlocks.SHAFT)

            addAll(RagiumBlocks.Decorations.entries)
            addAll(RagiumBlocks.LEDBlocks.entries)

            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)

            addAll(RagiumAPI.getInstance().machineRegistry.blocks) // TODO
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
            add(drum.get()) { block: Block ->
                LootTable
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
                                                .include(RagiumComponentTypes.DRUM_CONTENT.get()),
                                        ),
                                ),
                        ),
                    )
            }
        }
    }

    override fun getKnownBlocks(): Iterable<Block> = BuiltInRegistries.BLOCK
        .holders()
        .filter { holder: Holder.Reference<Block> -> holder.id?.namespace == RagiumAPI.MOD_ID }
        .map(Holder.Reference<Block>::value)
        .toList()
}
