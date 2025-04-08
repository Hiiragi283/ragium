package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        casings(output)
        machines(output)

        wells(output)
    }

    private fun casings(output: RecipeOutput) {
        // Wooden
        HTShapedRecipeBuilder(RagiumBlocks.WOODEN_CASING, 4)
            .cross8()
            .define('A', ItemTags.LOGS)
            .define('B', ItemTags.PLANKS)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Stone
        HTShapedRecipeBuilder(RagiumBlocks.STONE_CASING, 4)
            .casing()
            .define('A', Tags.Items.COBBLESTONES_NORMAL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Items.SMOOTH_STONE)
            .save(output)
        // Machine
        HTShapedRecipeBuilder(RagiumBlocks.MACHINE_CASING)
            .casing()
            .define('A', HTTagPrefixes.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', Blocks.DEEPSLATE_TILES)
            .save(output)
        // Advanced Machine
        HTShapedRecipeBuilder(RagiumBlocks.ADVANCED_MACHINE_CASING)
            .casing()
            .define('A', HTTagPrefixes.INGOT, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .define('C', RagiumBlocks.AZURE_TILE_SETS.base)
            .save(output)
        // Device
        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTTagPrefixes.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.DEVICE_CASING, 4)
            .cross8()
            .define('A', Tags.Items.OBSIDIANS_NORMAL)
            .define('B', HTTagPrefixes.INGOT, RagiumMaterials.DEEP_STEEL)
            .define('C', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_deep_steel")
    }

    private fun machines(output: RecipeOutput) {
        // Circuit
        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .define('B', HTTagPrefixes.DUST, VanillaMaterials.REDSTONE)
            .define('C', HTTagPrefixes.INGOT, VanillaMaterials.IRON)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .cross8()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.AZURE_STEEL)
            .define('B', HTTagPrefixes.DUST, VanillaMaterials.GLOWSTONE)
            .define('C', RagiumItemTags.CIRCUITS_BASIC)
            .saveSuffixed(output, "_from_basic")

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .define('B', HTTagPrefixes.DUST, VanillaMaterials.REDSTONE)
            .define('C', RagiumItemTags.PLASTICS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT, 4)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', HTTagPrefixes.INGOT, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .define('B', HTTagPrefixes.DUST, RagiumMaterials.RAGI_CRYSTAL)
            .define('C', RagiumItemTags.PLASTICS)
            .saveSuffixed(output, "_with_ragi")

        // Machine
        fun basicMachine(machine: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(machine)
                .addIngredient(RagiumItemTags.CIRCUITS_BASIC)
                .addIngredient(RagiumBlocks.MACHINE_CASING)
                .addIngredient(part)
                .save(output)
        }

        basicMachine(RagiumBlocks.CRUSHER, Ingredient.of(RagiumItemTags.TOOLS_FORGE_HAMMER))
        basicMachine(RagiumBlocks.EXTRACTOR, Ingredient.of(Items.HOPPER))

        fun advMachine(machine: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(machine)
                .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.ADVANCED_MACHINE_CASING)
                .addIngredient(part)
                .save(output)
        }

        advMachine(RagiumBlocks.ADVANCED_EXTRACTOR, Ingredient.of(Items.HOPPER))
        advMachine(RagiumBlocks.INFUSER, Ingredient.of(Items.DISPENSER))
        advMachine(RagiumBlocks.REFINERY, Ingredient.of(Tags.Items.GLASS_BLOCKS))

        HTSmithingRecipeBuilder(RagiumBlocks.ADVANCED_EXTRACTOR)
            .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
            .addIngredient(RagiumBlocks.EXTRACTOR)
            .saveSuffixed(output, "_from_basic")
    }

    private fun wells(output: RecipeOutput) {
        // Milk Drain
        HTShapedRecipeBuilder(RagiumBlocks.MILK_DRAIN)
            .pattern("A", "B", "C")
            .define('A', HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .define('B', Tags.Items.BARRELS_WOODEN)
            .define('C', RagiumBlocks.STONE_CASING)
            .save(output)
        // Soul Spike
        HTShapedRecipeBuilder(RagiumBlocks.SOUL_SPIKE)
            .pattern(
                " A ",
                "ABA",
                "BCB",
            ).define('A', Items.POINTED_DRIPSTONE)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .define('C', Tags.Items.NETHER_STARS)
            .save(output)

        // Basic
        fun basicDevice(device: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumItemTags.CIRCUITS_BASIC)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        basicDevice(RagiumBlocks.ITEM_COLLECTOR, Ingredient.of(Items.HOPPER))
        basicDevice(RagiumBlocks.SPRINKLER, Ingredient.of(Tags.Items.STORAGE_BLOCKS_BONE_MEAL))
        basicDevice(RagiumBlocks.WATER_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_WATER))

        // Advanced
        fun advDevice(device: ItemLike, part: Ingredient) {
            HTSmithingRecipeBuilder(device)
                .addIngredient(RagiumItemTags.CIRCUITS_ADVANCED)
                .addIngredient(RagiumBlocks.DEVICE_CASING)
                .addIngredient(part)
                .save(output)
        }

        fun advDevice(device: ItemLike, prefix: HTTagPrefix, key: HTMaterialKey) {
            advDevice(device, Ingredient.of(prefix.createItemTag(key)))
        }

        advDevice(RagiumBlocks.ENI, HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.RAGI_CRYSTAL)
        advDevice(RagiumBlocks.EXP_COLLECTOR, Ingredient.of(Items.HOPPER))
        advDevice(RagiumBlocks.LAVA_COLLECTOR, Ingredient.of(Tags.Items.BUCKETS_LAVA))
        advDevice(RagiumBlocks.TELEPORT_ANCHOR, HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.WARPED_CRYSTAL)

        // Elite
        fun catalyst(catalyst: ItemLike, key: HTMaterialKey, core: Ingredient) {
            HTShapedRecipeBuilder(catalyst)
                .cross8()
                .define('A', RagiumBlocks.DEVICE_CASING)
                .define('B', HTTagPrefixes.STORAGE_BLOCK, key)
                .define('C', core)
                .save(output)
        }

        catalyst(RagiumBlocks.RAGIUM_CATALYST, RagiumMaterials.RAGI_CRYSTAL, Ingredient.of(Tags.Items.NETHER_STARS))
        catalyst(RagiumBlocks.AZURE_CATALYST, RagiumMaterials.AZURE_STEEL, Ingredient.of(Items.HEART_OF_THE_SEA))
        catalyst(RagiumBlocks.DEEP_CATALYST, VanillaMaterials.DIAMOND, Ingredient.of(Tags.Items.NETHER_STARS))
    }
}
