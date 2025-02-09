package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTAlternativeRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Skulls
        fun skull(input: ItemLike, skull: Item) {
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(Items.SKELETON_SKULL)
                .itemInput(input, 8)
                .itemOutput(skull)
                .save(output)
        }

        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Tags.Items.STORAGE_BLOCKS_BONE_MEAL)
            .itemOutput(Items.SKELETON_SKULL)
            .save(output)

        skull(RagiumItems.WITHER_REAGENT, Items.WITHER_SKELETON_SKULL)
        skull(Items.GOLDEN_APPLE, Items.PLAYER_HEAD)
        skull(Items.ROTTEN_FLESH, Items.ZOMBIE_HEAD)
        skull(RagiumItems.CREEPER_REAGENT, Items.CREEPER_HEAD)
        skull(Items.GILDED_BLACKSTONE, Items.PIGLIN_HEAD)

        // Blaze Powder
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.CRIMSON_CRYSTAL),
                Items.BLAZE_POWDER,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.CRIMSON_CRYSTAL))
            .save(output)
        // Ender Pearl
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.WARPED_CRYSTAL),
                Items.ENDER_PEARL,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.WARPED_CRYSTAL))
            .save(output)

        // Mushroom Stew
        HTInfuserRecipeBuilder()
            .itemInput(Tags.Items.MUSHROOMS, 2)
            .milkInput()
            .itemOutput(Items.MUSHROOM_STEW, 2)
            .save(output)
        // Pumpkin Pie
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.CROPS_PUMPKIN)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .itemOutput(Items.PUMPKIN_PIE, 2)
            .save(output)

        // Mud
        HTInfuserRecipeBuilder()
            .itemInput(Items.DIRT)
            .waterInput()
            .itemOutput(Items.MUD)
            .save(output)
        // Packed Mud
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(Items.MUD)
            .itemOutput(Items.PACKED_MUD)
            .save(output)

        // Iron Bars
        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, Items.IRON_BARS, 8)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', HTTagPrefix.ROD, VanillaMaterials.IRON)
            .unlockedBy("has_iron_rod", has(HTTagPrefix.ROD, VanillaMaterials.IRON))

        // Copper Grate
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, Items.COPPER_GRATE, 4)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.ROD, VanillaMaterials.COPPER)
            .define('B', RagiumItems.FORGE_HAMMER)
            .unlockedBy("has_rod", has(HTTagPrefix.ROD, VanillaMaterials.COPPER))
            .save(output, RagiumAPI.id("shaped/copper_grate"))

        // Obsidian
        HTMixerRecipeBuilder()
            .waterInput()
            .fluidInput(Tags.Fluids.LAVA)
            .itemOutput(Items.OBSIDIAN)
            .save(output)

        registerSnow(output)
    }

    private fun registerSnow(output: RecipeOutput) {
        // Water -> Ice
        HTRefineryRecipeBuilder()
            .waterInput()
            .itemOutput(Items.SNOW_BLOCK)
            .save(output)

        // Snow Block -> 4x Snow Ball
        HTGrinderRecipeBuilder()
            .itemInput(Items.SNOW_BLOCK)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_block")
        // Ice -> 4x Snow Ball
        HTGrinderRecipeBuilder()
            .itemInput(Items.ICE)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_ice")

        // Powder Snow
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.BUCKETS_POWDER_SNOW)
            .itemOutput(Items.BUCKET)
            .fluidOutput(RagiumFluids.SNOW)
            .save(output, RagiumAPI.id("powder_snow"))

        HTInfuserRecipeBuilder()
            .itemInput(Items.BUCKET)
            .fluidInput(RagiumFluids.SNOW)
            .itemOutput(Items.POWDER_SNOW_BUCKET)
            .save(output)
    }
}
