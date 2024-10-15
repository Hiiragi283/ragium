package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.util.BothEither
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey

object HTMetalItemRecipeRegistry {
    private val registry: MutableMap<String, HTPropertyHolder> = mutableMapOf()

    @JvmStatic
    fun register(key: String, builderAction: HTPropertyHolder.Mutable.() -> Unit = {}) {
        registry[key] = HTPropertyHolder.create(builderAction = builderAction)
    }

    //    keys    //

    @JvmField
    val BLOCK: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("block")

    @JvmField
    val DISABLE_BLOCK_RECIPE: HTPropertyKey.Simple<Unit> =
        HTPropertyKey.Simple(RagiumAPI.id("disable_block_recipe"))

    @JvmField
    val DUST: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("dust")

    @JvmField
    val INGOT: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("ingot")

    @JvmField
    val ORE: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("ore")

    @JvmField
    val ORE_SUB_PRODUCTS: HTPropertyKey.Simple<ItemConvertible> =
        HTPropertyKey.Simple(RagiumAPI.id("ore_sub_products"))

    @JvmField
    val PLATE: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("plate")

    @JvmField
    val RAW: HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> = createKey("raw")

    @JvmStatic
    private fun createKey(name: String): HTPropertyKey.Simple<BothEither<ItemConvertible, TagKey<Item>>> =
        HTPropertyKey.Simple(RagiumAPI.id(name))

    //    Data Gen    //

    @JvmStatic
    fun generateRecipes(exporter: RecipeExporter, wrapper: (RecipeExporter, Boolean) -> RecipeExporter) {
        registry.values.forEach { properties: HTPropertyHolder ->
            ingotToBlockRecipe(exporter, properties)
            blockToIngotRecipe(exporter, properties)
            ingotToDustRecipe(exporter, properties)
            ingotToPlateRecipe(exporter, wrapper, properties)
            plateToDustRecipe(exporter, properties)

            oreToRawRecipe(exporter, properties)
            rawToDustRecipe(exporter, properties)
            rawToIngotRecipe(exporter, properties)
            dustToIngotRecipe(exporter, properties)
        }
    }

    @JvmStatic
    private fun ingotToBlockRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        if (DISABLE_BLOCK_RECIPE in properties) return
        val block: ItemConvertible = properties[BLOCK]?.getLeft() ?: return
        val ingot: BothEither<ItemConvertible, TagKey<Item>> = properties[INGOT] ?: return
        // Shaped Crafting
        HTShapedRecipeJsonBuilder
            .create(block)
            .patterns(
                "AAA",
                "AAA",
                "AAA",
            ).input('A', ingot)
            .criterion(ingot)
            .offerTo(exporter)
    }

    @JvmStatic
    private fun blockToIngotRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        if (DISABLE_BLOCK_RECIPE in properties) return
        val ingot: ItemConvertible = properties[INGOT]?.getLeft() ?: return
        val block: BothEither<ItemConvertible, TagKey<Item>> = properties[BLOCK] ?: return
        // Shapeless Crafting
        HTShapelessRecipeJsonBuilder
            .create(ingot, 9)
            .input(block)
            .criterion(block)
            .offerTo(exporter)
    }

    @JvmStatic
    private fun ingotToDustRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST]?.getLeft() ?: return
        val ingot: BothEither<ItemConvertible, TagKey<Item>> = properties[INGOT] ?: return
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(ingot)
            .addOutput(dust)
            .offerSuffix(exporter, "_from_ingot")
    }

    @JvmStatic
    private fun ingotToPlateRecipe(
        exporter: RecipeExporter,
        wrapper: (RecipeExporter, Boolean) -> RecipeExporter,
        properties: HTPropertyHolder,
    ) {
        val plate: ItemConvertible = properties[PLATE]?.getLeft() ?: return
        val ingot: BothEither<ItemConvertible, TagKey<Item>> = properties[INGOT] ?: return
        // Shaped Crafting (only hard mode)
        HTShapedRecipeJsonBuilder
            .create(plate)
            .patterns(
                "A",
                "B",
                "B",
            ).input('A', RagiumContents.Misc.FORGE_HAMMER)
            .input('B', ingot)
            .criterion(ingot)
            .offerTo(wrapper(exporter, true))
        // Metal Former Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .addInput(ingot)
            .addOutput(plate)
            // .setCatalyst(RagiumItems.PLATE_SHAPE)
            .offerSuffix(exporter)
    }

    @JvmStatic
    private fun plateToDustRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST]?.getLeft() ?: return
        val plate: BothEither<ItemConvertible, TagKey<Item>> = properties[PLATE] ?: return
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(plate)
            .addOutput(dust)
            .offerSuffix(exporter, "_from_plate")
    }

    @JvmStatic
    private fun oreToRawRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val rawMaterial: ItemConvertible = properties[RAW]?.getLeft() ?: return
        val ore: BothEither<ItemConvertible, TagKey<Item>> = properties[ORE] ?: return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(ore)
            .addOutput(rawMaterial)
            .addOutput(rawMaterial)
            .apply {
                properties[ORE_SUB_PRODUCTS]?.let { addOutput(it, 2) }
            }.offerSuffix(exporter)
    }

    @JvmStatic
    private fun rawToDustRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST]?.getLeft() ?: return
        val rawMaterial: BothEither<ItemConvertible, TagKey<Item>> = properties[RAW] ?: return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .addInput(rawMaterial)
            .addOutput(dust)
            .addOutput(dust)
            .offerSuffix(exporter)
    }

    @JvmStatic
    private fun rawToIngotRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val ingot: ItemConvertible = properties[INGOT]?.getLeft() ?: return
        val rawMaterial: BothEither<ItemConvertible, TagKey<Item>> = properties[RAW] ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            rawMaterial.getMapped(Ingredient::ofItems, Ingredient::fromTag, BothEither.Priority.RIGHT),
            ingot,
            rawMaterial.getMapped(
                RecipeProvider::conditionsFromItem,
                RecipeProvider::conditionsFromTag,
                BothEither.Priority.RIGHT,
            ),
            suffix = "_from_raw",
        )
    }

    @JvmStatic
    private fun dustToIngotRecipe(exporter: RecipeExporter, properties: HTPropertyHolder) {
        val ingot: ItemConvertible = properties[INGOT]?.getLeft() ?: return
        val dust: BothEither<ItemConvertible, TagKey<Item>> = properties[DUST] ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            dust.getMapped(Ingredient::ofItems, Ingredient::fromTag, BothEither.Priority.RIGHT),
            ingot,
            dust.getMapped(
                RecipeProvider::conditionsFromItem,
                RecipeProvider::conditionsFromTag,
                BothEither.Priority.RIGHT,
            ),
            suffix = "_from_dust",
        )
    }
}
