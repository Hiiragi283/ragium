package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Netherite Scrap
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            exp = fraction(1f)
            recipeId suffix "_from_ore"
        }

        stones()
        planks()
    }

    @JvmStatic
    private fun stones() {
        // Stone -> Cobblestone
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            result = resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            result = resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            result = resultCreator.create(Items.SAND, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_RED_BLOCKS)
            result = resultCreator.create(Items.RED_SAND, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }
    }

    @JvmStatic
    private fun planks() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(tagKey, input)
                result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, output)
                recipeId suffix "_from_${tagKey.location().path}"
            }
        }

        wood(ItemTags.BOATS, 1, 5)
        wood(ItemTags.LOGS_THAT_BURN, 1, 6)
        wood(ItemTags.WOODEN_BUTTONS, 1, 1)
        wood(ItemTags.WOODEN_DOORS, 1, 2)
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2)
        wood(ItemTags.WOODEN_SLABS, 2, 1)
        wood(ItemTags.WOODEN_STAIRS, 4, 3)
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3)
        wood(Tags.Items.BARRELS_WOODEN, 1, 7)
        wood(Tags.Items.CHESTS_WOODEN, 1, 8)
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4)
        wood(Tags.Items.FENCES_WOODEN, 1, 5)
        wood(Tags.Items.RODS_WOODEN, 2, 1)
    }
}
