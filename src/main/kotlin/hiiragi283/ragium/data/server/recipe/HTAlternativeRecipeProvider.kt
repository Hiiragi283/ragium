package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
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
            ).save(output)
        // Ender Pearl
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.WARPED_CRYSTAL),
                Items.ENDER_PEARL,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output)

        // Mushroom Stew
        HTFluidOutputRecipeBuilder
            .infuser()
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

        // Sand + Water -> Clay Block
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(Tags.Items.SANDS)
            .waterInput()
            .itemOutput(Items.CLAY)
            .save(output)

        // Dirt + Water -> Mud
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(Items.DIRT)
            .waterInput(250)
            .itemOutput(Items.MUD)
            .save(output)
        // Packed Mud
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(Items.MUD)
            .itemOutput(Items.PACKED_MUD)
            .save(output)

        // Iron Bars
        /*ShapedRecipeBuilder
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
            .save(output, RagiumAPI.id("shaped/copper_grate"))*/

        // Obsidian
        HTFluidOutputRecipeBuilder
            .mixer()
            .waterInput()
            .fluidInput(Tags.Fluids.LAVA)
            .itemOutput(Items.OBSIDIAN)
            .save(output)
    }
}
