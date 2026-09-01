package hiiragi283.ragium.common.material

import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.mapValue
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTMaterialAccess
import hiiragi283.ragium.api.material.HTMaterialContents
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HTMaterialAccessImpl : HTMaterialAccess {
    companion object {
        @JvmField
        val VANILLA_BLOCKS: HTMaterialContents<HTBlockPart, HTMaterialContents.BlockEntry> = HTMaterialContentsImpl(
            buildTable {
                @Suppress("DEPRECATION")
                fun accept(part: HTBlockPart, material: RagiumMaterial, block: Block) {
                    this[part, material] = block.builtInRegistryHolder()
                        .getKeyOrThrow()
                        .let(::HTSimpleDeferredBlockAndItem)
                        .let { HTMaterialContents.BlockEntry(it, true) }
                }

                // Fuels
                accept(HTBlockPart.ORE, RagiumMaterial.Fuel.COAL, Blocks.COAL_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Fuel.COAL, Blocks.DEEPSLATE_COAL_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Fuel.COAL, Blocks.COAL_BLOCK)
                // Mineral
                accept(HTBlockPart.ORE, RagiumMaterial.Mineral.REDSTONE, Blocks.REDSTONE_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Mineral.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Mineral.REDSTONE, Blocks.REDSTONE_BLOCK)

                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Mineral.GLOWSTONE, Blocks.GLOWSTONE)
                // Gem
                accept(HTBlockPart.ORE, RagiumMaterial.Gem.LAPIS, Blocks.LAPIS_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Gem.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Gem.LAPIS, Blocks.LAPIS_BLOCK)

                accept(HTBlockPart.NETHER_ORE, RagiumMaterial.Gem.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Gem.QUARTZ, Blocks.QUARTZ_BLOCK)

                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Gem.AMETHYST, Blocks.AMETHYST_BLOCK)

                accept(HTBlockPart.ORE, RagiumMaterial.Gem.DIAMOND, Blocks.DIAMOND_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Gem.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Gem.DIAMOND, Blocks.DIAMOND_BLOCK)

                accept(HTBlockPart.ORE, RagiumMaterial.Gem.EMERALD, Blocks.EMERALD_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Gem.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Gem.EMERALD, Blocks.EMERALD_BLOCK)
                // Metal
                accept(HTBlockPart.ORE, RagiumMaterial.Metal.COPPER, Blocks.COPPER_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Metal.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
                accept(HTBlockPart.RAW_STORAGE_BLOCK, RagiumMaterial.Metal.COPPER, Blocks.RAW_COPPER_BLOCK)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Metal.COPPER, Blocks.COPPER_BLOCK)

                accept(HTBlockPart.ORE, RagiumMaterial.Metal.IRON, Blocks.IRON_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Metal.IRON, Blocks.DEEPSLATE_IRON_ORE)
                accept(HTBlockPart.RAW_STORAGE_BLOCK, RagiumMaterial.Metal.IRON, Blocks.RAW_IRON_BLOCK)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Metal.IRON, Blocks.IRON_BLOCK)

                accept(HTBlockPart.ORE, RagiumMaterial.Metal.GOLD, Blocks.GOLD_ORE)
                accept(HTBlockPart.DEEPSLATE_ORE, RagiumMaterial.Metal.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
                accept(HTBlockPart.NETHER_ORE, RagiumMaterial.Metal.GOLD, Blocks.NETHER_GOLD_ORE)
                accept(HTBlockPart.RAW_STORAGE_BLOCK, RagiumMaterial.Metal.GOLD, Blocks.RAW_GOLD_BLOCK)
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Metal.GOLD, Blocks.GOLD_BLOCK)
                // Alloy
                accept(HTBlockPart.STORAGE_BLOCK, RagiumMaterial.Metal.NETHERITE, Blocks.NETHERITE_BLOCK)
            },
        ) { part: HTBlockPart, material: RagiumMaterial -> "Unknown $part block for $material" }

        @JvmField
        val VANILLA_ITEMS: HTMaterialContents<HTItemPart, HTMaterialContents.ItemEntry> = HTMaterialContentsImpl(
            buildTable {
                @Suppress("DEPRECATION")
                fun accept(part: HTItemPart, material: RagiumMaterial, item: Item) {
                    this[part, material] = item.builtInRegistryHolder()
                        .getKeyOrThrow()
                        .let(::HTSimpleDeferredItem)
                        .let { HTMaterialContents.ItemEntry(it, true) }
                }

                // Mineral
                accept(HTItemPart.DUST, RagiumMaterial.Mineral.REDSTONE, Items.REDSTONE)
                accept(HTItemPart.DUST, RagiumMaterial.Mineral.GLOWSTONE, Items.GLOWSTONE_DUST)
                // Gem
                accept(HTItemPart.GEM, RagiumMaterial.Gem.LAPIS, Items.LAPIS_LAZULI)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.QUARTZ, Items.QUARTZ)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.AMETHYST, Items.AMETHYST_SHARD)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.DIAMOND, Items.DIAMOND)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.EMERALD, Items.EMERALD)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.ECHO, Items.ECHO_SHARD)
                accept(HTItemPart.DUST, RagiumMaterial.Gem.PRISMARINE, Items.PRISMARINE_SHARD)
                accept(HTItemPart.GEM, RagiumMaterial.Gem.PRISMARINE, Items.PRISMARINE_CRYSTALS)
                // Metal
                accept(HTItemPart.RAW, RagiumMaterial.Metal.COPPER, Items.RAW_COPPER)
                accept(HTItemPart.INGOT, RagiumMaterial.Metal.COPPER, Items.COPPER_INGOT)
                accept(HTItemPart.NUGGET, RagiumMaterial.Metal.COPPER, Items.COPPER_NUGGET)

                accept(HTItemPart.RAW, RagiumMaterial.Metal.IRON, Items.RAW_IRON)
                accept(HTItemPart.INGOT, RagiumMaterial.Metal.IRON, Items.IRON_INGOT)
                accept(HTItemPart.NUGGET, RagiumMaterial.Metal.IRON, Items.IRON_NUGGET)

                accept(HTItemPart.RAW, RagiumMaterial.Metal.GOLD, Items.RAW_GOLD)
                accept(HTItemPart.INGOT, RagiumMaterial.Metal.GOLD, Items.GOLD_INGOT)
                accept(HTItemPart.NUGGET, RagiumMaterial.Metal.GOLD, Items.GOLD_NUGGET)
                // Alloy
                accept(HTItemPart.INGOT, RagiumMaterial.Metal.NETHERITE, Items.NETHERITE_INGOT)
            },
        ) { part: HTItemPart, material: RagiumMaterial -> "Unknown $part item for $material" }
    }

    override val existing: HTMaterialContents.Provider = HTMaterialContents.Provider(VANILLA_BLOCKS, VANILLA_ITEMS)
    override val registered: HTMaterialContents.Provider by lazy {
        HTMaterialContents.Provider(
            HTMaterialContentsImpl(
                RagiumBlocks.MATERIAL_BLOCKS.mapValue { (_, _, block: HTSimpleDeferredBlockAndItem) -> HTMaterialContents.BlockEntry(block, false) },
            ) { part: HTBlockPart, material: RagiumMaterial -> "Unregistered $part block for $material" },
            HTMaterialContentsImpl(
                RagiumItems.MATERIAL_ITEMS.mapValue { (_, _, item: HTSimpleDeferredItem) -> HTMaterialContents.ItemEntry(item, false) },
            ) { part: HTItemPart, material: RagiumMaterial -> "Unregistered $part item for $material" },
        )
    }
}
