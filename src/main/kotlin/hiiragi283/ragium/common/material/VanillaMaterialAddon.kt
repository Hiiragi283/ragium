package hiiragi283.ragium.common.material

import hiiragi283.lib.material.HTMaterialAddon
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.material.part.HTPartKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

data object VanillaMaterialAddon : HTMaterialAddon {
    override val priority: Int = 1000

    //    Part    //

    //    Material    //

    override fun registerExistingBlock(consumer: HTMaterialAddon.BlockConsumer) {
        @Suppress("DEPRECATION")
        fun accept(part: HTPartKey, key: HTMaterialKey, block: Block) {
            consumer.accept(part, key, block.builtInRegistryHolder().key())
        }

        // Fuels
        accept(CommonParts.ORE, VanillaMaterialKeys.COAL, Blocks.COAL_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COAL, Blocks.DEEPSLATE_COAL_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COAL, Blocks.COAL_BLOCK)
        // Mineral
        accept(CommonParts.ORE, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        accept(CommonParts.ORE, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_BLOCK)

        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.QUARTZ, Blocks.QUARTZ_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.AMETHYST, Blocks.AMETHYST_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        accept(CommonParts.ORE, VanillaMaterialKeys.COPPER, Blocks.COPPER_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.COPPER, Blocks.RAW_COPPER_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COPPER, Blocks.COPPER_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.IRON, Blocks.IRON_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.IRON, Blocks.DEEPSLATE_IRON_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.IRON, Blocks.RAW_IRON_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.IRON, Blocks.IRON_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.GOLD, Blocks.GOLD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.GOLD, Blocks.NETHER_GOLD_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.GOLD, Blocks.RAW_GOLD_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        accept(CommonParts.BLOCK, VanillaMaterialKeys.NETHERITE, Blocks.NETHERITE_BLOCK)
    }

    override fun registerExistingItem(consumer: HTMaterialAddon.ItemConsumer) {
        @Suppress("DEPRECATION")
        fun accept(part: HTPartKey, key: HTMaterialKey, item: Item) {
            consumer.accept(part, key, item.builtInRegistryHolder().key())
        }

        // Fuel
        accept(CommonParts.FUEL, VanillaMaterialKeys.COAL, Items.COAL)
        accept(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL)
        // Mineral
        accept(CommonParts.DUST, VanillaMaterialKeys.REDSTONE, Items.REDSTONE)
        accept(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        accept(CommonParts.GEM, VanillaMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        accept(CommonParts.GEM, VanillaMaterialKeys.QUARTZ, Items.QUARTZ)
        accept(CommonParts.GEM, VanillaMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.DIAMOND, Items.DIAMOND)
        accept(CommonParts.GEM, VanillaMaterialKeys.EMERALD, Items.EMERALD)
        accept(CommonParts.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD)
        accept(CommonParts.DUST, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_CRYSTALS)
        accept(CommonParts.GEM, VanillaMaterialKeys.ENDER, Items.ENDER_PEARL)
        // Metal
        accept(CommonParts.RAW, VanillaMaterialKeys.COPPER, Items.RAW_COPPER)
        accept(CommonParts.INGOT, VanillaMaterialKeys.COPPER, Items.COPPER_INGOT)

        accept(CommonParts.RAW, VanillaMaterialKeys.IRON, Items.RAW_IRON)
        accept(CommonParts.INGOT, VanillaMaterialKeys.IRON, Items.IRON_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.IRON, Items.IRON_NUGGET)

        accept(CommonParts.RAW, VanillaMaterialKeys.GOLD, Items.RAW_GOLD)
        accept(CommonParts.INGOT, VanillaMaterialKeys.GOLD, Items.GOLD_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.GOLD, Items.GOLD_NUGGET)
        // Alloy
        accept(CommonParts.INGOT, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)
        // Other
        accept(CommonParts.DUST, VanillaMaterialKeys.BLAZE, Items.BLAZE_POWDER)
        accept(CommonParts.ROD, VanillaMaterialKeys.BLAZE, Items.BLAZE_ROD)

        accept(CommonParts.DUST, VanillaMaterialKeys.BREEZE, Items.WIND_CHARGE)
        accept(CommonParts.ROD, VanillaMaterialKeys.BREEZE, Items.BREEZE_ROD)
    }

    override fun modifyMaterial(provider: HTMaterialAddon.MaterialProvider) {
    }
}
