package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.data.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.extension.isPopulated
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.HTDefaultMachineRecipe
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Rarity
import java.util.function.Function

object DefaultMaterialPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMaterial(helper: RagiumPlugin.MaterialHelper) {
        // alloy
        helper.register(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY, Rarity.RARE)
        helper.register(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY, Rarity.RARE)
        helper.register(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY, Rarity.UNCOMMON)

        helper.register(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY, Rarity.UNCOMMON)
        // dust
        helper.register(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        helper.register(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        // gem
        helper.register(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM, Rarity.COMMON)
        helper.register(RagiumMaterialKeys.NETHER_STAR, HTMaterialType.GEM, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.RUBY, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM, Rarity.RARE)
        // metal
        helper.register(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.GOLD, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        helper.register(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL, Rarity.EPIC)

        helper.register(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.SILVER, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // mineral
        helper.register(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        helper.register(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
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

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, ItemConvertible>) {
        fun bindContents(contents: List<HTMaterialProvider>) {
            contents.forEach { consumer.accept(it.tagPrefix, it.material, it) }
        }
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.DEEPSLATE_COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DEEPSLATE_DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.DEEPSLATE_EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.DEEPSLATE_LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.DEEPSLATE_REDSTONE_ORE)

        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.REDSTONE, Items.REDSTONE)

        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.COAL, Items.COAL)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.NETHER_STAR, Items.NETHER_STAR)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_NUGGET)

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.QUARTZ, Items.NETHER_QUARTZ_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.REDSTONE_ORE)

        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        // consumer.accept(HTTagPrefix.ROD, RagiumMaterialKeys.WOOD, Items.STICK)

        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COAL, Items.COAL_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items.COPPER_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.EMERALD, Items.EMERALD_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.GOLD, Items.GOLD_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON, Items.IRON_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.LAPIS, Items.LAPIS_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_BLOCK)

        bindContents(RagiumBlocks.Ores.entries)
        bindContents(RagiumBlocks.StorageBlocks.entries)
        bindContents(RagiumItems.Dusts.entries)
        bindContents(RagiumItems.Gears.entries)
        bindContents(RagiumItems.Gems.entries)
        bindContents(RagiumItems.Ingots.entries)
        bindContents(RagiumItems.Plates.entries)
        bindContents(RagiumItems.RawMaterials.entries)
    }

    /*override fun registerRuntimeRecipe(
        exporter: RecipeExporter,
        lookup: RegistryWrapper.WrapperLookup,
        helper: RagiumPlugin.RecipeHelper,
    ) {
        lookup.getWrapperOrThrow(RegistryKeys.FLUID).streamEntries().forEach { entry: RegistryEntry<Fluid> ->
            val id: Identifier = entry.id ?: return@forEach
            val fluid: Fluid = entry.value()
            if (!fluid.isStill(fluid.defaultState)) return@forEach
            // insert to cube
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.INFUSER, ::HTDefaultMachineRecipe)
                .itemInput(RagiumItems.EMPTY_FLUID_CUBE)
                .fluidInput(fluid)
                .itemOutput(RagiumAPI.getInstance().createFilledCube(fluid))
                .offerTo(exporter, id.withPrefixedPath("filling_cube/"))
            val bucket: Item = fluid.bucketItem
            if (bucket.isAir) return@forEach
            // insert to bucket
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.INFUSER, ::HTDefaultMachineRecipe)
                .itemInput(Items.BUCKET)
                .fluidInput(fluid)
                .itemOutput(bucket)
                .offerTo(exporter, id.withPrefixedPath("filling_bucket/"))
            // extract from bucket
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.EXTRACTOR, ::HTDefaultMachineRecipe)
                .itemInput(bucket)
                .itemOutput(Items.BUCKET)
                .fluidOutput(fluid)
                .offerTo(exporter, id.withPrefixedPath("extract_bucket/"))
        }
    }*/

    override fun registerRuntimeMaterialRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RagiumPlugin.RecipeHelper,
    ) {
        if (HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING !in entry) {
            // ingot/gem -> block
            helper.useItemIfPresent(entry, HTTagPrefix.STORAGE_BLOCK) { output: Item ->
                val prefix: HTTagPrefix = entry.type.getMainPrefix() ?: return@useItemIfPresent
                // Shaped Crafting
                HTShapedRecipeJsonBuilder
                    .create(output)
                    .pattern3x3()
                    .input('A', prefix, key)
                    .offerTo(exporter)
            }
            // block -> ingot/gem
            helper.useItemFromMainPrefix(entry) { output: Item ->
                // Shapeless Crafting
                HTShapelessRecipeJsonBuilder
                    .create(output, 9)
                    .input(HTTagPrefix.STORAGE_BLOCK, key)
                    .offerTo(exporter)
            }
        }

        // ingot/gem -> plate
        entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
            if (entry.type.isValidPrefix(HTTagPrefix.PLATE)) {
                // Compressor Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumRecipeTypes.COMPRESSOR)
                    .itemInput(prefix, key)
                    .catalyst(RagiumItems.PressMolds.PLATE)
                    .itemOutput(HTTagPrefix.PLATE, key)
                    .offerTo(exporter, HTTagPrefix.PLATE, key)
                // Cutting Machine Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumRecipeTypes.CUTTING_MACHINE)
                    .itemInput(HTTagPrefix.STORAGE_BLOCK, key)
                    .catalyst(RagiumItems.PressMolds.PLATE)
                    .itemOutput(HTTagPrefix.PLATE, key, 9)
                    .offerTo(exporter, HTTagPrefix.PLATE, key)
            }
        }

        if (entry.type.isValidPrefix(HTTagPrefix.DUST)) {
            // ingot/gem -> dust
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                HTMachineRecipeJsonBuilder
                    .create(RagiumRecipeTypes.GRINDER)
                    .itemInput(prefix, key)
                    .itemOutput(HTTagPrefix.DUST, key)
                    .offerTo(exporter, HTTagPrefix.DUST, key, "_from_${prefix.asString()}")
            }
            // plate -> dust
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.GRINDER)
                .itemInput(HTTagPrefix.PLATE, key)
                .itemOutput(HTTagPrefix.DUST, key)
                .offerTo(exporter, HTTagPrefix.DUST, key, "_from_plate")
            // gear -> dust
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.GRINDER)
                .itemInput(HTTagPrefix.GEAR, key)
                .itemOutput(HTTagPrefix.DUST, key, 4)
                .offerTo(exporter, HTTagPrefix.DUST, key, "_from_gear")
            // raw -> dust
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.GRINDER)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key)
                .itemOutput(HTTagPrefix.DUST, key, 2)
                .offerTo(exporter, HTTagPrefix.DUST, key, "_from_raw")
        }

        // ingot/gem -> gear
        entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
            helper.useItemIfPresent(entry, HTTagPrefix.GEAR) { output: Item ->
                // Shaped Recipe
                HTShapedRecipeJsonBuilder
                    .create(output)
                    .patterns(
                        " A ",
                        "ABA",
                        " A ",
                    ).input('A', prefix, key)
                    .input('B', HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON)
                    .offerTo(exporter)
            }
            if (entry.type.isValidPrefix(HTTagPrefix.GEAR)) {
                // Compressor Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumRecipeTypes.COMPRESSOR)
                    .itemInput(prefix, key, 4)
                    .catalyst(RagiumItems.PressMolds.GEAR)
                    .itemOutput(HTTagPrefix.GEAR, key)
                    .offerTo(exporter, HTTagPrefix.GEAR, key)
            }
        }

        // ingot/gem -> rod
        entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
            if (entry.type.isValidPrefix(HTTagPrefix.ROD)) {
                HTMachineRecipeJsonBuilder
                    .create(RagiumRecipeTypes.COMPRESSOR)
                    .itemInput(prefix, key)
                    .catalyst(RagiumItems.PressMolds.ROD)
                    .itemOutput(HTTagPrefix.ROD, key, 2)
                    .offerTo(exporter, HTTagPrefix.ROD, key)
            }
        }

        // ingot -> wire
        if (entry.type.isValidPrefix(HTTagPrefix.WIRE)) {
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.COMPRESSOR)
                .itemInput(HTTagPrefix.INGOT, key)
                .catalyst(RagiumItems.PressMolds.WIRE)
                .itemOutput(HTTagPrefix.WIRE, key, 3)
                .offerTo(exporter, HTTagPrefix.WIRE, key)
        }

        // ore -> raw/gem
        helper.useItemFromRawPrefix(entry) { output: Item ->
            val count: Int = entry.getOrDefault(HTMaterialPropertyKeys.GRINDING_BASE_COUNT)
            val subProduction: ItemConvertible? = entry[HTMaterialPropertyKeys.ORE_SUB_PRODUCT]
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.GRINDER)
                .itemInput(HTTagPrefix.ORE, key)
                .itemOutput(output, count * 2)
                .itemOutput(RagiumItems.SLAG)
                .apply { subProduction?.let(::itemOutput) }
                .offerTo(exporter, HTTagPrefix.ORE, key, "_2x")
            // 3x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, ::HTDefaultMachineRecipe)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
                .itemOutput(output, count * 3)
                .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidConstants.INGOT)
                .apply { subProduction?.let(::itemOutput) }
                .offerTo(exporter, HTTagPrefix.ORE, key, "_3x")
            // 4x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, ::HTDefaultMachineRecipe, HTMachineTier.BASIC)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
                .itemOutput(output, count * 4)
                .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidConstants.INGOT)
                .apply { subProduction?.let { itemOutput(it, 2) } }
                .offerTo(exporter, HTTagPrefix.ORE, key, "_4x")
            // 5x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, ::HTDefaultMachineRecipe, HTMachineTier.ADVANCED)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluids.MERCURY, FluidConstants.INGOT)
                .itemOutput(output, count * 5)
                .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidConstants.INGOT)
                .offerTo(exporter, HTTagPrefix.ORE, key, "_5x")
        }

        if (HTMaterialPropertyKeys.DISABLE_RAW_SMELTING !in entry) {
            // raw -> ingot
            helper.useItemIfPresent(entry, HTTagPrefix.INGOT) { output: Item ->
                val input: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
                if (!input.isPopulated()) return@useItemIfPresent
                HTCookingRecipeJsonBuilder.smeltAndBlast(
                    exporter,
                    input,
                    output,
                    entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                    suffix = "_from_raw",
                )
            }
            // dust -> ingot
            helper.useItemIfPresent(entry, HTTagPrefix.INGOT) { output: Item ->
                val input: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
                if (!input.isPopulated()) return@useItemIfPresent
                HTCookingRecipeJsonBuilder.smeltAndBlast(
                    exporter,
                    input,
                    output,
                    entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                    suffix = "_from_dust",
                )
            }
        }

        // dust -> gem
        if (entry.type.isValidPrefix(HTTagPrefix.GEM)) {
            HTMachineRecipeJsonBuilder
                .create(RagiumRecipeTypes.COMPRESSOR)
                .itemInput(HTTagPrefix.DUST, key)
                .itemOutput(HTTagPrefix.GEM, key)
                .offerTo(exporter, HTTagPrefix.GEM, key)
        }
    }
}
