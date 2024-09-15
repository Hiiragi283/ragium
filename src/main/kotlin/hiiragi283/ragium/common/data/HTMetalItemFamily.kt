package hiiragi283.ragium.common.data

import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTMetalItemFamily(val name: String, variants: Map<Variant, Item>, private val excludeVariants: Set<Variant>) :
    Map<HTMetalItemFamily.Variant, Item> by variants {
    companion object {
        val registry: Map<String, HTMetalItemFamily>
            get() = instances
        private val instances: MutableMap<String, HTMetalItemFamily> = mutableMapOf()
    }

    init {
        check(name !in registry) { "Family; $name is already registered!" }
        instances[name] = this
    }

    private fun isExcluded(variant: Variant): Boolean = variant in excludeVariants

    /*fun getTagKey(variant: Variant): TagKey<Item> = TagKey.of(
        RegistryKeys.ITEM,
        Identifier.of(TagUtil.C_TAG_NAMESPACE, "${variant.tagPrefix}$name")
    )*/

    fun generateRecipes(exporter: RecipeExporter, wrapper: (RecipeExporter, Boolean) -> RecipeExporter) {
        ingotToBlockRecipe(exporter)
        blockToIngotRecipe(exporter)

        ingotToPlateRecipe(exporter, wrapper)
        ingotToRodRecipe(exporter, wrapper)

        oreToRawRecipe(exporter)
        rawToDustRecipe(exporter)
        dustToIngotRecipe(exporter)
    }

    private fun ingotToBlockRecipe(exporter: RecipeExporter) {
        if (isExcluded(Variant.BLOCK)) return
        val block: Item = get(Variant.BLOCK) ?: return
        val ingot: Item = get(Variant.INGOT) ?: return
        // Shaped Crafting
        ShapedRecipeJsonBuilder
            .create(RecipeCategory.MISC, block)
            .pattern("AAA")
            .pattern("AAA")
            .pattern("AAA")
            .input('A', ingot)
            .criterion("has_ingot", RecipeProvider.conditionsFromItem(ingot))
            .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(block).withPrefixedPath("shaped/"))
    }

    private fun blockToIngotRecipe(exporter: RecipeExporter) {
        if (isExcluded(Variant.INGOT)) return
        val ingot: Item = get(Variant.INGOT) ?: return
        val block: Item = get(Variant.BLOCK) ?: return
        // Shapeless Crafting
        ShapelessRecipeJsonBuilder
            .create(RecipeCategory.MISC, ingot, 9)
            .input(block)
            .criterion("has_block", RecipeProvider.conditionsFromItem(block))
            .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(ingot).withPrefixedPath("shapeless/"))
    }

    private fun ingotToPlateRecipe(exporter: RecipeExporter, wrapper: (RecipeExporter, Boolean) -> RecipeExporter) {
        val plate: Item = get(Variant.PLATE) ?: return
        val plateId: Identifier = CraftingRecipeJsonBuilder.getItemId(plate)
        val ingot: Item = get(Variant.INGOT) ?: return
        // Smithing Recipe (only hard mode)
        SmithingTransformRecipeJsonBuilder
            .create(
                Ingredient.ofItems(RagiumItems.FORGE_HAMMER),
                Ingredient.ofItems(ingot),
                Ingredient.ofItems(ingot),
                RecipeCategory.MISC,
                plate,
            ).criterion("has_ingot", RecipeProvider.conditionsFromItem(ingot))
            .offerTo(wrapper(exporter, true), plateId.withPrefixedPath("smithing/"))
        // Metal Former Recipe
        HTMachineRecipeJsonBuilder(HTMachineType.Single.METAL_FORMER)
            .addInput(ingot)
            .addOutput(plate)
            .setCatalyst(RagiumItems.PLATE_SHAPE)
            .offerTo(exporter, plateId)
    }

    private fun ingotToRodRecipe(exporter: RecipeExporter, wrapper: (RecipeExporter, Boolean) -> RecipeExporter) {
        val rod: Item = get(Variant.ROD) ?: return
        val rodId: Identifier = CraftingRecipeJsonBuilder.getItemId(rod)
        val ingot: Item = get(Variant.INGOT) ?: return
        // Smithing Recipe (only hard mode)
        SmithingTransformRecipeJsonBuilder
            .create(
                Ingredient.ofItems(RagiumItems.FORGE_HAMMER),
                Ingredient.ofItems(ingot),
                Ingredient.EMPTY,
                RecipeCategory.MISC,
                rod,
            ).criterion("has_ingot", RecipeProvider.conditionsFromItem(ingot))
            .offerTo(wrapper(exporter, true), rodId.withPrefixedPath("smithing/"))
        // Metal Former Recipe
        HTMachineRecipeJsonBuilder(HTMachineType.Single.METAL_FORMER)
            .addInput(ingot)
            .addOutput(rod, 2)
            .setCatalyst(RagiumItems.ROD_SHAPE)
            .offerTo(exporter, rodId)
    }

    private fun oreToRawRecipe(exporter: RecipeExporter) {
        val rawMaterial: Item = get(Variant.RAW_MATERIAL) ?: return
        val rawId: Identifier = CraftingRecipeJsonBuilder.getItemId(rawMaterial)
        val ore: Item = get(Variant.ORE) ?: return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder(HTMachineType.Single.GRINDER)
            .addInput(ore)
            .addOutput(rawMaterial)
            .addOutput(rawMaterial, 2)
            .offerTo(exporter, rawId)
    }

    private fun rawToDustRecipe(exporter: RecipeExporter) {
        val dust: Item = get(Variant.DUST) ?: return
        val dustId: Identifier = CraftingRecipeJsonBuilder.getItemId(dust)
        val rawMaterial: Item = get(Variant.RAW_MATERIAL) ?: return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder(HTMachineType.Single.GRINDER)
            .addInput(rawMaterial)
            .addOutput(dust)
            .addOutput(dust)
            .offerTo(exporter, dustId)
    }

    private fun dustToIngotRecipe(exporter: RecipeExporter) {
        val ingot: Item = get(Variant.INGOT) ?: return
        val ingotId: Identifier = CraftingRecipeJsonBuilder.getItemId(ingot)
        val dust: Item = get(Variant.DUST) ?: return
        // Furnace Recipe
        CookingRecipeJsonBuilder
            .createSmelting(
                Ingredient.ofItems(dust),
                RecipeCategory.MISC,
                ingot,
                0.0f,
                200,
            ).criterion("has_dust", RecipeProvider.conditionsFromItem(dust))
            .offerTo(exporter, ingotId.withPrefixedPath("smelting/"))
        // Blast Furnace Recipe
        CookingRecipeJsonBuilder
            .createBlasting(
                Ingredient.ofItems(dust),
                RecipeCategory.MISC,
                ingot,
                0.0f,
                100,
            ).criterion("has_dust", RecipeProvider.conditionsFromItem(dust))
            .offerTo(exporter, ingotId.withPrefixedPath("blasting/"))
    }

    //    Builder    //

    class Builder {
        private val variants: MutableMap<Variant, Item> = mutableMapOf()
        private val excludeVariants: MutableSet<Variant> = mutableSetOf()

        fun block(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.BLOCK, item, exclude)

        fun dust(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.DUST, item, exclude)

        fun gear(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.GEAR, item, exclude)

        fun ingot(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.INGOT, item, exclude)

        fun ore(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.ORE, item, exclude)

        fun plate(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.PLATE, item, exclude)

        fun rawMaterial(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.RAW_MATERIAL, item, exclude)

        fun rod(item: ItemConvertible, exclude: Boolean = false): Builder = add(Variant.ROD, item, exclude)

        private fun add(variant: Variant, item: ItemConvertible, exclude: Boolean): Builder = apply {
            val item1: Item = item.asItem()
            if (item1 != Items.AIR) {
                variants[variant] = item1
            }
            if (exclude) excludeVariants.add(variant)
        }

        fun build(name: String): HTMetalItemFamily = HTMetalItemFamily(name, variants, excludeVariants)
    }

    //    Variant    //

    enum class Variant {
        BLOCK,
        DUST,
        GEAR,
        INGOT,
        ORE,
        PLATE,
        RAW_MATERIAL,
        ROD,
        ;

        // val tagPrefix = "${name.lowercase()}s/"
        val allTagKey: TagKey<Item> =
            TagKey.of(
                RegistryKeys.ITEM,
                Identifier.of(TagUtil.C_TAG_NAMESPACE, "${name.lowercase()}s"),
            )
    }
}
