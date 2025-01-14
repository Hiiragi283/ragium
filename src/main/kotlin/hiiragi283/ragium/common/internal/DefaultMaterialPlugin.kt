package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.function.Function
import java.util.function.Supplier

object DefaultMaterialPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMaterial(helper: RagiumPlugin.MaterialHelper) {
        // alloy
        helper.register(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY)

        helper.register(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY)
        // dust
        helper.register(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        helper.register(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        // gem
        helper.register(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM)
        // helper.registe)
        helper.register(RagiumMaterialKeys.NETHERITE_SCRAP, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.RUBY, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM)
        // metal
        helper.register(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.GOLD, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        helper.register(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL)

        helper.register(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.SILVER, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // mineral
        helper.register(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        helper.register(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL)
        // plate
        // helper.register(RagiumMaterialKeys.STONE, HTMaterialType.PLATE)
        // helper.register(RagiumMaterialKeys.WOOD, HTMaterialType.PLATE)

        // alternative name
        // helper.addAltName(RagiumMaterialKeys.WOOD, "saw")
        helper.addAltName(RagiumMaterialKeys.ASH, "ashes")
        helper.addAltName(RagiumMaterialKeys.NITER, "saltpeter")
    }

    override fun setupMaterialProperties(helper: Function<HTMaterialKey, HTPropertyHolderBuilder>) {
        // metal
        helper
            .apply(RagiumMaterialKeys.COPPER)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)

        helper
            .apply(RagiumMaterialKeys.GOLD)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 1f)

        helper
            .apply(RagiumMaterialKeys.IRON)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)

        helper.apply(RagiumMaterialKeys.IRIDIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.TITANIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.TUNGSTEN).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.URANIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )
        // gem
        helper.apply(RagiumMaterialKeys.AMETHYST).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.COAL).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.EMERALD).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.DIAMOND).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper
            .apply(RagiumMaterialKeys.LAPIS)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 4)
        helper.apply(RagiumMaterialKeys.QUARTZ).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
        // mineral
        helper
            .apply(RagiumMaterialKeys.REDSTONE)
            .put(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 2)
            .put(HTMaterialPropertyKeys.ORE_SUB_PRODUCT, RagiumItems.Gems.CINNABAR)
    }

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, Supplier<out ItemLike>>) {
        fun <T> bindContents(contents: List<T>) where T : Supplier<out ItemLike>, T : HTMaterialProvider {
            contents.forEach { consumer.accept(it.tagPrefix, it.material, it) }
        }

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items::DEEPSLATE_COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items::DEEPSLATE_COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items::DEEPSLATE_DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items::DEEPSLATE_EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items::DEEPSLATE_GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items::DEEPSLATE_IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items::DEEPSLATE_LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items::DEEPSLATE_REDSTONE_ORE)

        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.REDSTONE, Items::REDSTONE)

        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.AMETHYST, Items::AMETHYST_SHARD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.COAL, Items::COAL)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items::DIAMOND)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items::EMERALD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.LAPIS, Items::LAPIS_LAZULI)
        // consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.NETHER_STAR, Items::NETHER_STAR)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP, Items::NETHERITE_SCRAP)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items::QUARTZ)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items::COPPER_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items::GOLD_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items::IRON_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.NETHERITE, Items::NETHERITE_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items::GOLD_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items::IRON_NUGGET)

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items::COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items::COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items::DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items::EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items::GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items::IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items::LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.NETHERITE_SCRAP, Items::ANCIENT_DEBRIS)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.QUARTZ, Items::NETHER_QUARTZ_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items::REDSTONE_ORE)

        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items::RAW_COPPER)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items::RAW_GOLD)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items::RAW_IRON)

        // consumer.accept(HTTagPrefix.ROD, RagiumMaterialKeys.WOOD, Items::STICK)

        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COAL, Items::COAL_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items::COPPER_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.DIAMOND, Items::DIAMOND_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.EMERALD, Items::EMERALD_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.GOLD, Items::GOLD_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON, Items::IRON_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.LAPIS, Items::LAPIS_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.NETHERITE, Items::NETHERITE_BLOCK)

        // bindContents(RagiumBlocks.Ores.entries)
        bindContents(RagiumBlocks.StorageBlocks.entries)
        bindContents(RagiumItems.Dusts.entries)
        bindContents(RagiumItems.Gears.entries)
        bindContents(RagiumItems.Gems.entries)
        bindContents(RagiumItems.Ingots.entries)
        // bindContents(RagiumItems.Plates.entries)
        bindContents(RagiumItems.RawMaterials.entries)
    }
}
