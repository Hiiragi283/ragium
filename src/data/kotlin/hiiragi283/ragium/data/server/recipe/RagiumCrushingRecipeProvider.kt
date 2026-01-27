package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        ores()
        stones()
        planks()
    }

    @JvmStatic
    private fun ores() {
        // Coal
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL)
            result = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) to fraction(1, 4)
            exp = fraction(0.3f)
            recipeId suffix "_from_ore"
        }
        // Redstone
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 8)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CINNABAR)
            exp = fraction(0.5f)
            recipeId suffix "_from_ore"
        }
        // Cinnabar
        // Salt
        // Saltpeter
        // Sulfur
        // Lapis
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.LAPIS, 12)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER)
            exp = fraction(0.7f)
            recipeId suffix "_from_ore"
        }
        // Quartz
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ, 6)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) to fraction(1, 4)
            exp = fraction(0.7f)
            recipeId suffix "_from_ore"
        }
        // Amethyst
        // Diamond
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL) to fraction(1, 4)
            exp = fraction(1f)
            recipeId suffix "_from_ore"
        }
        // Emerald
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.EMERALD, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.PRISMARINE) to fraction(1, 4)
            exp = fraction(1f)
            recipeId suffix "_from_ore"
        }

        // Copper
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER, 3)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD) to fraction(1, 4)
            exp = fraction(0.3f)
            recipeId suffix "_from_ore"
        }
        // Iron
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.TIN) to fraction(1, 4)
            exp = fraction(0.5f)
            recipeId suffix "_from_ore"
        }
        // Zinc
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, CommonMaterialKeys.ZINC)
            result = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.ZINC, 2)
            // chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER) to fraction(1, 4)
            exp = fraction(0.3f)
            recipeId suffix "_from_ore"
        }
        // Tin
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, CommonMaterialKeys.TIN)
            result = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.TIN, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON) to fraction(1, 4)
            exp = fraction(0.3f)
            recipeId suffix "_from_ore"
        }
        // Gold
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER) to fraction(1, 4)
            exp = fraction(0.7f)
            recipeId suffix "_from_ore"
        }
        // Netherite
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            exp = fraction(1f)
            recipeId suffix "_from_ore"
        }
        // Deep Steel

        // Raginite
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGINITE)
            result = resultCreator.material(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8)
            exp = fraction(0.5f)
            recipeId suffix "_from_ore"
        }
        // Ragi-Crystal
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGI_CRYSTAL)
            result = resultCreator.material(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE) to fraction(1, 4)
            exp = fraction(1f)
            recipeId suffix "_from_ore"
        }
    }

    @JvmStatic
    private fun stones() {
        // Stone -> Cobblestone
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            result = resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            result = resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            result = resultCreator.create(Items.SAND, 2)
            chancedResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }

        HTChancedRecipeBuilder.crushing(output) {
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
            HTChancedRecipeBuilder.crushing(this.output) {
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
