package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
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
import net.neoforged.neoforge.registries.DeferredBlock

class RagiumBlockLootProvider(provider: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(Items.BEDROCK), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            remove(RagiumBlocks.CRUDE_OIL)
            remove(RagiumBlocks.COPPER_DRUM)
            removeAll(RagiumBlocks.ORES.values)
        }.forEach { dropSelf(it.get()) }

        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            val prefix: HTTagPrefix = when (key) {
                RagiumMaterials.RAGINITE -> HTTagPrefix.RAW_MATERIAL
                RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
                else -> error("Undefined material: $key")
            }
            val rawMaterial: ItemLike = RagiumItems.getMaterialItem(prefix, key)
            add(ore.get()) { block: Block -> createOreDrop(block, rawMaterial.asItem()) }
        }

        add(RagiumBlocks.COPPER_DRUM.get()) {
            copyComponent(
                it,
                RagiumComponentTypes.FLUID_CONTENT.get(),
                DataComponents.ENCHANTMENTS,
            )
        }

        HTMachineType.getBlocks().forEach { holder: DeferredBlock<*> ->
            add(holder.get()) { copyComponent(it, DataComponents.ENCHANTMENTS) }
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
