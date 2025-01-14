package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.requires
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.core.Holder
import net.minecraft.data.recipes.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTMaterialRecipeProvider : RecipeProviderChild {
    private lateinit var output: RecipeOutput

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
        RagiumItems.Gems.entries.forEach { ingot: RagiumItems.Gems ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, ingot.material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, ingot.material))
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

            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, gear)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', mainPrefix, material)
                .define('B', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_input", has(mainPrefix, material))
                .savePrefixed(output)
        }

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

    private fun oreToDustRecipe(key: HTMaterialKey, entry: HTMaterialRegistry.Entry) {
        if (!entry.type.isValidPrefix(HTTagPrefix.ORE)) return
        val raw: Holder<Item> = entry.getFirstItemOrNull(HTTagPrefix.RAW_MATERIAL) ?: return
        val subProduct: ItemLike? = entry[HTMaterialPropertyKeys.ORE_SUB_PRODUCT]
        // Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(raw, 2)
            .save(output)
        // 3x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.SIMPLE)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME / 10)
            .itemOutput(raw, 3)
            .apply { subProduct?.let(this::itemOutput) }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 10)
            .saveSuffixed(output, "_3x")
        // 4x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.SIMPLE)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME / 5)
            .itemOutput(raw, 4)
            .apply { subProduct?.let { itemOutput(it, 2) } }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 5)
            .saveSuffixed(output, "_4x")
        // 5x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.SIMPLE)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumFluids.MERCURY, FluidType.BUCKET_VOLUME / 2)
            .itemOutput(raw, 5)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 2)
            .saveSuffixed(output, "_5x")

        // Grinder
        val dust: Holder<Item> = entry.getFirstItemOrNull(HTTagPrefix.DUST) ?: return
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.RAW_MATERIAL, key)
            .itemOutput(dust, 2)
            .saveSuffixed(output, "_from_raw")
    }
}
