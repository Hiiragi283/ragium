package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.data.HTRecipeJsonBuilder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generate(exporter: RecipeExporter) {
        craftingRecipes(exporter)
        cookingRecipes(exporter)

        grinderRecipes(exporter)
        alloyFurnaceRecipes(exporter)
    }

    private fun <T : CraftingRecipeJsonBuilder> T.itemCriterion(item: ItemConvertible): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromItem(item))
    }

    private fun <T : CraftingRecipeJsonBuilder> T.tagCriterion(tagKey: TagKey<Item>): T = apply {
        criterion("has_input", RecipeProvider.conditionsFromTag(tagKey))
    }

    private fun RecipeExporter.conditions(vararg conditions: ResourceCondition): RecipeExporter =
        withConditions(this, *conditions)

    private fun craftingRecipes(exporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .input('A', RagiumItems.RAW_RAGINITE)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumItems.RAW_RAGINITE)
            .offerTo(exporter, Ragium.id("shaped/ragi_alloy_compound"))

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .group("ragi_alloy_compound")
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .input('A', RagiumItems.RAW_RAGINITE_DUST)
            .input('B', ConventionalItemTags.COPPER_INGOTS)
            .itemCriterion(RagiumItems.RAW_RAGINITE_DUST)
            .offerTo(exporter, Ragium.id("shaped/ragi_alloy_compound_1"))
    }

    private fun cookingRecipes(exporter: RecipeExporter) {
        CookingRecipeJsonBuilder.createSmelting(
            Ingredient.ofItems(RagiumItems.RAGI_ALLOY_COMPOUND),
            RecipeCategory.MISC,
            RagiumItems.RAGI_ALLOY_INGOT,
            0.0f,
            200
        )
            .itemCriterion(RagiumItems.RAGI_ALLOY_COMPOUND)
            .offerTo(exporter, Ragium.id("smelting/ragi_alloy_ingot"))
    }

    private fun grinderRecipes(exporter: RecipeExporter) {
        grinderRecipe(exporter, ConventionalItemTags.QUARTZ_ORES, Items.QUARTZ, 2)
        grinderRecipe(exporter, ConventionalItemTags.RED_SANDSTONE_BLOCKS, Items.RED_SAND, 4)
        grinderRecipe(exporter, ConventionalItemTags.SANDSTONE_BLOCKS, Items.SAND, 4)
        grinderRecipe(exporter, Items.COARSE_DIRT, Items.DIRT)
        grinderRecipe(exporter, Items.COBBLESTONE, Items.GRAVEL)
        grinderRecipe(exporter, Items.DEEPSLATE, Items.DEEPSLATE)
        grinderRecipe(exporter, Items.GRAVEL, Items.SAND, id = Identifier.of("gravel_to_sand"))
        grinderRecipe(exporter, Items.STONE, Items.COBBLESTONE)
        grinderRecipe(exporter, ItemTags.COAL_ORES, Items.COAL, 2)
        grinderRecipe(exporter, ItemTags.COPPER_ORES, Items.RAW_COPPER, 4)
        grinderRecipe(exporter, ItemTags.DIAMOND_ORES, Items.DIAMOND, 2)
        grinderRecipe(exporter, ItemTags.EMERALD_ORES, Items.EMERALD, 2)
        grinderRecipe(exporter, ItemTags.GOLD_ORES, Items.RAW_GOLD, 2)
        grinderRecipe(exporter, ItemTags.IRON_ORES, Items.RAW_IRON, 2)
        grinderRecipe(exporter, ItemTags.LAPIS_ORES, Items.LAPIS_LAZULI, 8)
        grinderRecipe(exporter, ItemTags.REDSTONE_ORES, Items.REDSTONE, 8)
        grinderRecipe(exporter, ItemTags.WOOL, Items.STRING, 4)

        grinderRecipe(exporter, RagiumItems.RAW_RAGINITE, RagiumItems.RAW_RAGINITE_DUST)
    }

    private fun grinderRecipe(
        exporter: RecipeExporter,
        input: ItemConvertible,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
    ) {
        HTRecipeJsonBuilder(HTMachineType.GRINDER)
            .addInput(input)
            .addOutput(output, count)
            .hasInput(input)
            .offerTo(exporter, id)
    }

    private fun grinderRecipe(
        exporter: RecipeExporter,
        input: TagKey<Item>,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
    ) {
        HTRecipeJsonBuilder(HTMachineType.GRINDER)
            .addInput(input)
            .addOutput(output, count)
            .hasInput(input)
            .offerTo(exporter, id)
    }

    private fun alloyFurnaceRecipes(exporter: RecipeExporter) {
        HTRecipeJsonBuilder(HTMachineType.ALLOY_FURNACE)
            .addInput(ConventionalItemTags.COPPER_INGOTS)
            .addInput(RagiumItems.RAW_RAGINITE_DUST, 4)
            .addOutput(RagiumItems.RAGI_ALLOY_INGOT)
            .hasInput(RagiumItems.RAW_RAGINITE_DUST)
            .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(RagiumItems.RAGI_ALLOY_INGOT))
    }

}