package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.requires
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.*
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidType

object HTMaterialRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        // Ingot/Gem -> Block
        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            val material: HTMaterialKey = storage.material
            val mainPrefix: HTTagPrefix = when (storage) {
                RagiumBlocks.StorageBlocks.FLUORITE -> HTTagPrefix.GEM
                RagiumBlocks.StorageBlocks.RAGI_CRYSTAL -> HTTagPrefix.GEM
                RagiumBlocks.StorageBlocks.CRYOLITE -> HTTagPrefix.GEM
                else -> HTTagPrefix.INGOT
            }

            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', mainPrefix, material)
                .unlockedBy("has_input", has(mainPrefix, material))
                .savePrefixed(output)
        }
        // Block -> Ingot
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, ingot.material)
                .unlockedBy("has_ingot", has(HTTagPrefix.STORAGE_BLOCK, ingot.material))
                .savePrefixed(output)
        }
        // Block -> Gem
        RagiumItems.RawResources.entries.forEach { resource: RagiumItems.RawResources ->
            if (resource.tagPrefix != HTTagPrefix.GEM) return@forEach
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, resource, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, resource.material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, resource.material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems.Gears.entries.forEach { gear: RagiumItems.Gears ->
            val material: HTMaterialKey = gear.material
            val mainPrefix: HTTagPrefix = when (gear) {
                RagiumItems.Gears.DIAMOND -> HTTagPrefix.GEM
                RagiumItems.Gears.EMERALD -> HTTagPrefix.GEM
                else -> HTTagPrefix.INGOT
            }
            // Shaped Recipe
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, gear)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', mainPrefix, material)
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_input", has(mainPrefix, material))
                .savePrefixed(output)
            // Compressor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(mainPrefix, material, 4)
                .catalyst(RagiumItems.GEAR_PRESS_MOLD)
                .itemOutput(gear)
                .save(output)
        }

        // Ingot/Gem -> Rod
        RagiumItems.Rods.entries.forEach { rod: RagiumItems.Rods ->
            val material: HTMaterialKey = rod.material
            val mainPrefix: HTTagPrefix = when (rod) {
                RagiumItems.Rods.DIAMOND -> HTTagPrefix.GEM
                RagiumItems.Rods.EMERALD -> HTTagPrefix.GEM
                else -> HTTagPrefix.INGOT
            }
            // Shaped Recipe
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, rod, 2)
                .pattern("AB")
                .pattern("A ")
                .define('A', mainPrefix, material)
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_input", has(mainPrefix, material))
                .savePrefixed(output)
            // Compressor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(mainPrefix, material)
                .catalyst(RagiumItems.ROD_PRESS_MOLD)
                .itemOutput(rod, 2)
                .save(output)
        }

        // Ore -> Raw/Gem
        registerOreToRaw(output, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        registerOreToRaw(output, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        registerOreToRaw(output, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        registerOreToRaw(output, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        registerOreToRaw(output, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        registerOreToRaw(output, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI, 4)
        registerOreToRaw(output, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        registerOreToRaw(output, RagiumMaterialKeys.REDSTONE, Items.REDSTONE, 2, RagiumItems.RawResources.CINNABAR)
        registerOreToRaw(output, RagiumMaterialKeys.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)

        RagiumItems.RawResources.entries.forEach { raw: RagiumItems.RawResources ->
            registerOreToRaw(output, raw.material, raw)
            if (raw.tagPrefix == HTTagPrefix.GEM) {
                registerDustToGem(output, raw.material, raw)
            }
        }

        // Raw/Ingot/Gem -> Dust
        registerRawToDust(output, RagiumMaterialKeys.REDSTONE, Items.REDSTONE)

        RagiumItems.Dusts.entries.forEach { dust: RagiumItems.Dusts ->
            if (dust.originPrefix == null) return@forEach
            registerRawToDust(output, dust.material, dust)
            registerInputToDust(output, dust, dust)
        }

        // Dust -> Gem
        registerDustToGem(output, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        registerDustToGem(output, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        registerDustToGem(output, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        registerDustToGem(output, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        // Raw/Dust -> Ingot
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            val material: HTMaterialKey = ingot.material
            // Smelting
            SimpleCookingRecipeBuilder
                .smelting(
                    Ingredient.of(HTTagPrefix.DUST.createTag(material)),
                    RecipeCategory.MISC,
                    ingot,
                    0.5f,
                    200,
                ).unlockedBy("has_raw", has(HTTagPrefix.DUST, material))
                .save(output, ingot.id.withPath { "smelting/${it}_from_dust" })
            // Blasting
            SimpleCookingRecipeBuilder
                .blasting(
                    Ingredient.of(HTTagPrefix.DUST.createTag(material)),
                    RecipeCategory.MISC,
                    ingot,
                    0.5f,
                    100,
                ).unlockedBy("has_raw", has(HTTagPrefix.DUST, material))
                .save(output, ingot.id.withPath { "blasting/${it}_from_dust" })

            // Smelting
            SimpleCookingRecipeBuilder
                .smelting(
                    Ingredient.of(HTTagPrefix.RAW_MATERIAL.createTag(material)),
                    RecipeCategory.MISC,
                    ingot,
                    0.5f,
                    200,
                ).unlockedBy("has_raw", has(HTTagPrefix.RAW_MATERIAL, material))
                .save(output, ingot.id.withPath { "smelting/${it}_from_raw" })
            // Blasting
            SimpleCookingRecipeBuilder
                .blasting(
                    Ingredient.of(HTTagPrefix.RAW_MATERIAL.createTag(material)),
                    RecipeCategory.MISC,
                    ingot,
                    0.5f,
                    100,
                ).unlockedBy("has_raw", has(HTTagPrefix.RAW_MATERIAL, material))
                .save(output, ingot.id.withPath { "blasting/${it}_from_raw" })
        }
    }

    private fun registerOreToRaw(
        output: RecipeOutput,
        material: HTMaterialKey,
        raw: ItemLike,
        baseCount: Int = 1,
        subProduct: ItemLike? = null,
    ) {
        // 2x Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.ORE, material)
            .itemOutput(raw, baseCount * 2)
            .apply { subProduct?.let(this::itemOutput) }
            .itemOutput(RagiumItems.SLAG)
            .saveSuffixed(output, "_2x")
        // 3x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME / 10)
            .itemOutput(raw, baseCount * 3)
            .apply { subProduct?.let(this::itemOutput) }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 10)
            .saveSuffixed(output, "_3x")
        // 4x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME / 5)
            .itemOutput(raw, baseCount * 4)
            .apply { subProduct?.let { itemOutput(it, 2) } }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 5)
            .saveSuffixed(output, "_4x")
        // 5x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ULTIMATE)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.MERCURY, FluidType.BUCKET_VOLUME / 2)
            .itemOutput(raw, baseCount * 5)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 2)
            .saveSuffixed(output, "_5x")
    }

    private fun registerRawToDust(output: RecipeOutput, material: HTMaterialKey, dust: ItemLike) {
        // Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.RAW_MATERIAL, material)
            .itemOutput(dust, 2)
            .saveSuffixed(output, "_from_raw")
    }

    private fun registerDustToGem(output: RecipeOutput, material: HTMaterialKey, gem: ItemLike) {
        // Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.DUST, material)
            .itemOutput(gem)
            .saveSuffixed(output, "_from_dust")
    }

    private fun registerInputToDust(output: RecipeOutput, input: RagiumItems.Dusts, dust: ItemLike) {
        val origin: HTTagPrefix? = input.originPrefix
        if (origin == null) return
        // Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(origin, input.material)
            .itemOutput(dust)
            .saveSuffixed(output, "_from_${origin.serializedName}")
    }
}
