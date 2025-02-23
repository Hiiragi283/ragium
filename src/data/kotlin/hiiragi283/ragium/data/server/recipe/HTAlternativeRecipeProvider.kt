package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HTAlternativeRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Skulls
        fun skull(input: ItemLike, skull: Item) {
            HTMultiItemRecipeBuilder
                .assembler(lookup)
                .itemInput(Items.SKELETON_SKULL)
                .itemInput(input, 8)
                .itemOutput(skull)
                .save(output)
        }

        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Tags.Items.STORAGE_BLOCKS_BONE_MEAL)
            .itemOutput(Items.SKELETON_SKULL)
            .save(output)

        skull(RagiumItems.WITHER_REAGENT, Items.WITHER_SKELETON_SKULL)
        skull(Items.GOLDEN_APPLE, Items.PLAYER_HEAD)
        skull(Items.ROTTEN_FLESH, Items.ZOMBIE_HEAD)
        skull(Items.GUNPOWDER, Items.CREEPER_HEAD)
        skull(Items.GILDED_BLACKSTONE, Items.PIGLIN_HEAD)

        // Blaze Powder
        HTCookingRecipeBuilder
            .create(
                HTTagPrefix.GEM.createIngredient(RagiumMaterials.CRIMSON_CRYSTAL),
                Items.BLAZE_POWDER,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output)
        // Ender Pearl
        HTCookingRecipeBuilder
            .create(
                HTTagPrefix.GEM.createIngredient(RagiumMaterials.WARPED_CRYSTAL),
                Items.ENDER_PEARL,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output)

        // Mushroom Stew
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Tags.Items.MUSHROOMS, 2)
            .milkInput()
            .itemOutput(Items.MUSHROOM_STEW, 2)
            .save(output)
        // Pumpkin Pie
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(Tags.Items.CROPS_PUMPKIN)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .itemOutput(Items.PUMPKIN_PIE, 2)
            .save(output)

        // Sand + Water -> Clay Block
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Tags.Items.SANDS)
            .waterInput()
            .itemOutput(Items.CLAY)
            .save(output)

        // Dirt + Water -> Mud
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Items.DIRT)
            .waterInput(250)
            .itemOutput(Items.MUD)
            .save(output)
        // Packed Mud
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(Items.MUD)
            .itemOutput(Items.PACKED_MUD)
            .save(output)

        fun ironAlt(key: HTMaterialKey, modifier: Int) {
            val name: String = key.name
            // Bucket
            HTShapedRecipeBuilder(Items.BUCKET, modifier)
                .pattern(
                    "ABA",
                    " A ",
                ).define('A', HTTagPrefix.INGOT, key)
                .define('B', RagiumItems.FORGE_HAMMER)
                .save(output, RagiumAPI.id("bucket_by_$name"))
            // Hopper
            HTShapedRecipeBuilder(Items.HOPPER, modifier)
                .pattern(
                    "ABA",
                    "ACA",
                    " A ",
                ).define('A', HTTagPrefix.INGOT, key)
                .define('B', RagiumItems.FORGE_HAMMER)
                .define('C', Tags.Items.CHESTS)
                .save(output, RagiumAPI.id("hopper_by_$name"))
            // Piston
            HTShapedRecipeBuilder(Items.PISTON, modifier)
                .pattern(
                    "AAA",
                    "BCB",
                    "BDB",
                ).define('A', ItemTags.PLANKS)
                .define('B', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('C', HTTagPrefix.INGOT, key)
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .save(output, RagiumAPI.id("piston_by_$name"))
        }

        ironAlt(CommonMaterials.STEEL, 2)
        ironAlt(RagiumMaterials.DEEP_STEEL, 4)
    }
}
