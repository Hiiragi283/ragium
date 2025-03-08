package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.commonTag
import hiiragi283.ragium.api.extension.requires
import hiiragi283.ragium.api.extension.requiresFor
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.*
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.*
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

object HTCommonRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerRaginite(output)
        registerMetals(output)
        registerRagium(output)

        registerBurners(output)
        registerCrates(output)
        registerDrums(output)

        registerDecorations(output)
        registerLEDs(output)

        registerCircuits(output)
        registerPressMolds(output)
        registerLens(output)

        registerArmor(output)
        registerTool(output)
        registerMisc(output)
    }

    //    Progress    //

    private fun registerRaginite(output: RecipeOutput) {
        val ragiAlloy: DeferredItem<out Item> =
            RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)

        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow8()
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .save(output, RagiumAPI.id("ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                ragiAlloy,
                exp = 0.5f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output)

        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.COPPER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .itemOutput(ragiAlloy)
            .save(output)
        // Refined Ragi-Steel
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE, 5)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL))
            .save(output)
    }

    private fun registerMetals(output: RecipeOutput) {
        // Steel
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.STEEL_COMPOUND)
            .requires(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .requiresFor(4, HTTagPrefix.GEM.createIngredient(VanillaMaterials.COAL))
            .requires(RagiumItems.FORGE_HAMMER)
            .unlockedBy("has_iron", has(HTTagPrefix.DUST, VanillaMaterials.IRON))
            .savePrefixed(output)

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.STEEL_COMPOUND),
                RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL),
                time = 400,
                exp = 1f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output, RagiumAPI.id("steel_ingot_from_compound"))

        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.COAL, 2)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL))
            .save(output)

        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.COAL_COKE)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL))
            .saveSuffixed(output, "_with_coke")
        // Deep Steel
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.STEEL, 8)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.NIOBIUM)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL), 9)
            .save(output)

        // Ember Alloy
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.COPPER, 3)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.GOLD)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY), 4)
            .save(output)
        // Duralumin
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM, 7)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.COPPER)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.OBSIDIAN)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, RagiumMaterials.DURALUMIN), 9)
            .save(output)
    }

    private fun registerRagium(output: RecipeOutput) {
        // Ragium
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 8)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 1000)
            .fluidOutput(RagiumVirtualFluids.RAGIUM_SOLUTION)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION.commonTag, 8000)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM))
            .save(output)

        // Unbreakable Elytra
        val elytraId: ResourceLocation = RagiumAPI.id("smithing/ragi_elytra")
        val ragiumIngot: TagKey<Item> = HTTagPrefix.INGOT.createTag(RagiumMaterials.RAGIUM)
        output.accept(
            elytraId,
            SmithingTransformRecipe(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Items.ELYTRA),
                Ingredient.of(ragiumIngot),
                ItemStack(Items.ELYTRA).apply {
                    set(DataComponents.UNBREAKABLE, Unbreakable(true))
                },
            ),
            null,
        )
        // Ragi Ticket
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(RagiumItemTags.PAPER)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION.commonTag, 125)
            .itemOutput(RagiumItems.RAGI_TICKET)
            .save(output)
    }

    //    Components    //

    private fun registerBurners(output: RecipeOutput) {
        for (burner: DeferredBlock<Block> in RagiumBlocks.BURNERS) {
            val core: ItemLike = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> Items.MAGMA_BLOCK
                RagiumBlocks.SOUL_BURNER -> RagiumBlocks.SOUL_MAGMA_BLOCK
                RagiumBlocks.FIERY_BURNER -> RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!
                else -> continue
            }
            val base: Item = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> Items.POLISHED_BLACKSTONE_BRICKS
                RagiumBlocks.SOUL_BURNER -> Items.END_STONE_BRICKS
                RagiumBlocks.FIERY_BURNER -> Items.RED_NETHER_BRICKS
                else -> continue
            }
            // Shaped Crafting
            HTShapedRecipeBuilder(burner)
                .pattern("A A")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', Items.IRON_BARS)
                .define('B', core)
                .define('C', base)
                .save(output)
        }
    }

    private fun registerCrates(output: RecipeOutput) {
        for ((variant: HTCrateVariant, crate: DeferredBlock<HTCrateBlock>) in RagiumBlocks.CRATES) {
            // Shaped Crafting
            HTShapedRecipeBuilder(crate)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', variant.baseTag)
                .define('B', ItemTags.WOODEN_SLABS)
                .define('C', Tags.Items.BARRELS)
                .save(output)
        }
    }

    private fun registerDrums(output: RecipeOutput) {
        for ((variant: HTDrumVariant, drum: DeferredBlock<HTDrumBlock>) in RagiumBlocks.DRUMS) {
            // Shaped Crafting
            HTShapedRecipeBuilder(drum)
                .pattern(
                    "ABA",
                    "ACA",
                    "ABA",
                ).define('A', variant.baseTag)
                .define('B', Items.SMOOTH_STONE_SLAB)
                .define('C', Tags.Items.BUCKETS_EMPTY)
                .save(output)
        }
    }

    //    Decorations    //

    private fun registerDecorations(output: RecipeOutput) {
        // Ragi-Bricks
        HTShapedRecipeBuilder(RagiumBlocks.RAGI_BRICKS, 2, CraftingBookCategory.BUILDING)
            .cross8()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', Tags.Items.BRICKS_NORMAL)
            .define('C', Items.CLAY)
            .save(output)
        // Plastic Block
        HTShapedRecipeBuilder(RagiumBlocks.PLASTIC_BLOCK, 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', RagiumItemTags.PLASTICS)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
        // Blue Nether Bricks
        HTShapedRecipeBuilder(RagiumBlocks.BLUE_NETHER_BRICKS, category = CraftingBookCategory.BUILDING)
            .pattern(
                "AB",
                "BA",
            ).define('A', RagiumItemTags.CROPS_WARPED_WART)
            .define('B', Tags.Items.BRICKS_NETHER)
            .save(output)

        registerFamily(output, RagiumBlocks.RAGI_BRICK_SETS)
        registerFamily(output, RagiumBlocks.PLASTIC_SETS)
        registerFamily(output, RagiumBlocks.BLUE_NETHER_BRICK_SETS)
    }

    private fun registerFamily(output: RecipeOutput, family: HTBuildingBlockSets) {
        // Base -> Slab
        HTShapedRecipeBuilder(family.slab, 6, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.slab, 2)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(family.stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.stairs)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
        // Base -> Wall
        HTShapedRecipeBuilder(family.wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', family.base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(family.base), RecipeCategory.BUILDING_BLOCKS, family.wall)
            .unlockedBy("has_base", has(family.base))
            .savePrefixed(output)
    }

    private fun registerLEDs(output: RecipeOutput) {
        // LED
        HTShapedRecipeBuilder(RagiumBlocks.getLedBlock(DyeColor.WHITE), 4, CraftingBookCategory.BUILDING)
            .hollow4()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', RagiumItems.LED)
            .save(output, RagiumAPI.id("led_block"))

        for ((color: DyeColor, block: DeferredBlock<Block>) in RagiumBlocks.LED_BLOCKS) {
            // Shaped Crafting
            HTShapedRecipeBuilder(block, 4, CraftingBookCategory.BUILDING)
                .hollow8()
                .define('A', RagiumItemTags.LED_BLOCKS)
                .define('B', color.commonTag)
                .save(output)
        }
    }

    //    Ingredient    //

    private fun registerCircuits(output: RecipeOutput) {
        fun circuit(
            circuit: ItemLike,
            subMetal: HTMaterialKey,
            dopant: TagKey<Item>,
            lens: ItemLike,
        ) {
            // Assembler
            HTAssemblerRecipeBuilder(lookup)
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, subMetal)
                .itemInput(dopant)
                .itemOutput(circuit)
                .save(output)
            // Laser Assembly
            HTSingleItemRecipeBuilder
                .laser(lookup)
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .catalyst(lens)
                .itemOutput(circuit)
                .save(output)
        }

        circuit(
            RagiumItems.BASIC_CIRCUIT,
            VanillaMaterials.COPPER,
            Tags.Items.DUSTS_REDSTONE,
            RagiumItems.REDSTONE_LENS,
        )
        circuit(
            RagiumItems.ADVANCED_CIRCUIT,
            VanillaMaterials.GOLD,
            Tags.Items.DUSTS_GLOWSTONE,
            RagiumItems.GLOWSTONE_LENS,
        )
        circuit(
            RagiumItems.ELITE_CIRCUIT,
            CommonMaterials.ALUMINUM,
            Tags.Items.GEMS_PRISMARINE,
            RagiumItems.DIAMOND_LENS,
        )

        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', RagiumItemTags.CIRCUITS_BASIC)
            .save(output)
    }

    private fun registerPressMolds(output: RecipeOutput) {
        fun register(mold: ItemLike) {
            SingleItemRecipeBuilder
                .stonecutting(
                    Ingredient.of(RagiumItems.BLANK_PRESS_MOLD),
                    RecipeCategory.MISC,
                    mold,
                ).unlockedBy("has_steel", has(RagiumItems.BLANK_PRESS_MOLD))
                .savePrefixed(output)
        }

        HTShapedRecipeBuilder(RagiumItems.BLANK_PRESS_MOLD)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)

        register(RagiumItems.BALL_PRESS_MOLD)
        register(RagiumItems.BLOCK_PRESS_MOLD)
        register(RagiumItems.GEAR_PRESS_MOLD)
        register(RagiumItems.INGOT_PRESS_MOLD)
        register(RagiumItems.PLATE_PRESS_MOLD)
        register(RagiumItems.ROD_PRESS_MOLD)
        register(RagiumItems.WIRE_PRESS_MOLD)
    }

    private fun registerLens(output: RecipeOutput) {
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(Tags.Items.DUSTS_REDSTONE, 64)
            .itemInput(Tags.Items.INGOTS_COPPER, 16)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS, 8)
            .itemOutput(RagiumItems.REDSTONE_LENS)
            .save(output)

        HTAssemblerRecipeBuilder(lookup)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE, 64)
            .itemInput(Tags.Items.INGOTS_GOLD, 16)
            .itemInput(RagiumBlocks.QUARTZ_GLASS, 8)
            .itemOutput(RagiumItems.GLOWSTONE_LENS)
            .save(output)

        HTAssemblerRecipeBuilder(lookup)
            .itemInput(Tags.Items.GEMS_DIAMOND, 64)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 16)
            .itemInput(Tags.Items.GLASS_BLOCKS_TINTED, 8)
            .itemOutput(RagiumItems.DIAMOND_LENS)
            .save(output)

        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.BLOCK, VanillaMaterials.AMETHYST, 64)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.NETHERITE, 16)
            .itemInput(RagiumBlocks.OBSIDIAN_GLASS, 8)
            .itemOutput(RagiumItems.AMETHYST_LENS)
            .save(output)
    }

    //    Armor    //

    private fun registerArmor(output: RecipeOutput) {
        // Diving Goggles
        HTShapedRecipeBuilder(RagiumItems.DIVING_GOGGLE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
                "C C",
            ).define('A', RagiumItemTags.PLASTICS)
            .define('B', Tags.Items.GLASS_PANES)
            .define('C', RagiumItemTags.CIRCUITS_ADVANCED)
            .save(output)
        // Jetpack
        HTShapedRecipeBuilder(RagiumItems.JETPACK, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                "ACA",
                "D D",
            ).define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItemTags.CIRCUITS_ELITE)
            .define('C', ItemTags.CHEST_ARMOR)
            .define('D', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)

        armorSet(output, RagiumItems.EMBER_ALLOY_ARMORS)
        armorSet(output, RagiumItems.STEEL_ARMORS)
    }

    private fun armorSet(output: RecipeOutput, armorSet: HTArmorSets) {
        // Helmet
        HTShapedRecipeBuilder(armorSet[ArmorItem.Type.HELMET], category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
            ).define('A', HTTagPrefix.INGOT, armorSet.key)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
        // Chestplate
        HTShapedRecipeBuilder(armorSet[ArmorItem.Type.CHESTPLATE], category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                "AAA",
                "AAA",
            ).define('A', HTTagPrefix.INGOT, armorSet.key)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
        // Leggings
        HTShapedRecipeBuilder(armorSet[ArmorItem.Type.LEGGINGS], category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
                "A A",
            ).define('A', HTTagPrefix.INGOT, armorSet.key)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
        // Boots
        HTShapedRecipeBuilder(armorSet[ArmorItem.Type.BOOTS], category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A A",
                "ABA",
            ).define('A', HTTagPrefix.INGOT, armorSet.key)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)
    }

    //    Tool    //

    private fun registerTool(output: RecipeOutput) {
        HTShapedRecipeBuilder(RagiumItems.FORGE_HAMMER, category = CraftingBookCategory.EQUIPMENT)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ITEM_MAGNET, category = CraftingBookCategory.EQUIPMENT)
            .pattern("A A")
            .pattern("B B")
            .pattern(" B ")
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.POTION_BUNDLE, category = CraftingBookCategory.EQUIPMENT)
            .pattern("ABA")
            .pattern("CCC")
            .define('A', Tags.Items.STRINGS)
            .define('B', Tags.Items.LEATHERS)
            .define('C', Items.GLASS_BOTTLE)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN, category = CraftingBookCategory.EQUIPMENT)
            .hollow8()
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_SHEARS, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                " A",
                "A ",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.DURALUMIN_CASE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                " A ",
                "BCB",
                "BBB",
            ).define('A', HTTagPrefix.ROD, CommonMaterials.STEEL)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN)
            .define('C', HTTagPrefix.GEM, RagiumMaterials.WARPED_CRYSTAL)
            .save(output)

        // Custom Pickaxe
        HTShapedRecipeBuilder(RagiumItems.FEVER_PICKAXE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                " C ",
                " C ",
            ).define('A', HTTagPrefix.GEM, VanillaMaterials.EMERALD)
            .define('B', HTTagPrefix.BLOCK, VanillaMaterials.EMERALD)
            .define('C', Tags.Items.RODS_WOODEN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.SILKY_PICKAXE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                " C ",
                " C ",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.EMBER_ALLOY)
            .define('B', HTTagPrefix.BLOCK, RagiumMaterials.EMBER_ALLOY)
            .define('C', Tags.Items.RODS_WOODEN)
            .save(output)

        toolSet(output, RagiumItems.EMBER_ALLOY_TOOLS)
        toolSet(output, RagiumItems.STEEL_TOOLS)
    }

    private fun toolSet(output: RecipeOutput, toolSet: HTToolSets) {
        // Axe
        HTShapedRecipeBuilder(toolSet.axeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A ",
                "AB",
                "BB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, toolSet.key)
            .save(output)
        // Hoe
        HTShapedRecipeBuilder(toolSet.hoeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A ",
                "A ",
                "BB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, toolSet.key)
            .save(output)
        // Pickaxe
        HTShapedRecipeBuilder(toolSet.pickaxeItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                " A ",
                " A ",
                "BBB",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, toolSet.key)
            .save(output)
        // Shovel
        HTShapedRecipeBuilder(toolSet.shovelItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A",
                "A",
                "B",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, toolSet.key)
            .save(output)
        // Sword
        HTShapedRecipeBuilder(toolSet.swordItem, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A",
                "B",
                "B",
            ).define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, toolSet.key)
            .save(output)
    }

    //    Misc    //

    private fun registerMisc(output: RecipeOutput) {
        // Soul Magma Block
        HTShapedRecipeBuilder(RagiumBlocks.SOUL_MAGMA_BLOCK)
            .hollow4()
            .define('A', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .define('B', Items.MAGMA_BLOCK)
            .save(output)
        // Solar Panel
        HTShapedRecipeBuilder(RagiumItems.SOLAR_PANEL)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.GLASS_PANES)
            .define('B', HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .define('C', HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .save(output)

        HTAssemblerRecipeBuilder(lookup)
            .itemInput(Tags.Items.GLASS_PANES, 2)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.LAPIS, 2)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 2)
            .itemOutput(RagiumItems.SOLAR_PANEL)
            .save(output)
        // LED
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .itemOutput(RagiumItems.LED, 4)
            .save(output)
        // Engine
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 4)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY, 4)
            .itemInput(Items.PISTON, 2)
            .itemOutput(RagiumItems.ENGINE)
            .save(output)
        // Slag
        HTShapedRecipeBuilder(RagiumBlocks.SLAG_BLOCK)
            .hollow8()
            .define('A', RagiumItemTags.SLAG)
            .define('B', RagiumItems.SLAG)
            .save(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.SLAG, 9)
            .requires(RagiumBlocks.SLAG_BLOCK)
            .unlockedBy("has_slag", has(RagiumBlocks.SLAG_BLOCK))
            .savePrefixed(output)
        // Obsidian Dust
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, VanillaMaterials.OBSIDIAN), 4)
            .save(output)
    }
}
