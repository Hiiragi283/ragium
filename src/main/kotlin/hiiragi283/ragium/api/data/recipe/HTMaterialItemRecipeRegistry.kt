package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.recipe.HTIngredientNew
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeResultNew
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object HTMaterialItemRecipeRegistry {
    //    keys    //

    @JvmField
    val BLOCK: HTPropertyKey.Simple<ItemConvertible> = createKey("storage_block")

    @JvmField
    val DISABLE_BLOCK_RECIPE: HTPropertyKey.Simple<Unit> =
        HTPropertyKey.Simple(RagiumAPI.id("disable_block_recipe"))

    @JvmField
    val DUST: HTPropertyKey.Simple<ItemConvertible> = createKey("dust")

    @JvmField
    val INGOT: HTPropertyKey.Simple<ItemConvertible> = createKey("ingot")

    @JvmField
    val ORE: HTPropertyKey.Simple<ItemConvertible> = createKey("ore")

    @JvmField
    val ORE_SUB_PRODUCTS: HTPropertyKey.Simple<ItemConvertible> =
        HTPropertyKey.Simple(RagiumAPI.id("ore_sub_products"))

    @JvmField
    val PLATE: HTPropertyKey.Simple<ItemConvertible> = createKey("plate")

    @JvmField
    val RAW: HTPropertyKey.Simple<ItemConvertible> = createKey("raw_material")

    @JvmStatic
    private fun createKey(name: String): HTPropertyKey.Simple<ItemConvertible> = HTPropertyKey.Simple(RagiumAPI.id(name))

    @JvmField
    val EXCLUDED_KEYS: List<HTPropertyKey.Simple<out Any>> = listOf(DISABLE_BLOCK_RECIPE, ORE_SUB_PRODUCTS)

    //    Register    //

    @JvmStatic
    private val registry: MutableMap<String, HTPropertyHolder> = mutableMapOf()

    @JvmStatic
    fun getTagKey(name: String, key: HTPropertyKey<*>): TagKey<Item> =
        TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, "${key.id.path}s/$name"))

    @JvmStatic
    fun register(name: String, builderAction: HTPropertyHolder.Mutable.() -> Unit = {}) {
        registry[name] = HTPropertyHolder.create(builderAction = builderAction)
    }

    //    Data Gen - Tag    //

    @JvmStatic
    fun configureTags(consumer: (TagKey<Item>, ItemConvertible?) -> Unit) {
        registry.forEach { (name: String, properties: HTPropertyHolder) ->
            properties.forEach property@{ (key: HTPropertyKey<*>, value: Any) ->
                if (key in EXCLUDED_KEYS) return@property
                consumer(getTagKey(name, key), value as? ItemConvertible)
            }
        }
    }

    //    Data Gen - Recipe    //

    @JvmStatic
    fun generateRecipes(
        exporter: RecipeExporter,
        wrapper1: (RecipeExporter, Boolean) -> RecipeExporter,
        wrapper2: (RecipeExporter, TagKey<Item>) -> RecipeExporter,
    ) {
        registry.forEach { (name: String, properties: HTPropertyHolder) ->
            ingotToBlockRecipe(exporter, name, properties)
            blockToIngotRecipe(exporter, name, properties)
            ingotToDustRecipe(exporter, name, properties)
            ingotToPlateRecipe(exporter, wrapper1, name, properties)
            plateToDustRecipe(exporter, name, properties)

            oreToRawRecipe(exporter, name, properties)
            rawToDustRecipe(exporter, name, properties)
            rawToIngotRecipe(exporter, wrapper2, name, properties)
            dustToIngotRecipe(exporter, wrapper2, name, properties)
        }
    }

    @JvmStatic
    private fun ingotToBlockRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        if (DISABLE_BLOCK_RECIPE in properties) return
        val block: ItemConvertible = properties[BLOCK] ?: return
        val ingot: TagKey<Item> = getTagKey(name, INGOT)
        // Shaped Crafting
        HTShapedRecipeJsonBuilder
            .create(block)
            .patterns(
                "AAA",
                "AAA",
                "AAA",
            ).input('A', ingot)
            .unlockedBy(ingot)
            .offerTo(exporter)
    }

    @JvmStatic
    private fun blockToIngotRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        if (DISABLE_BLOCK_RECIPE in properties) return
        val ingot: ItemConvertible = properties[INGOT] ?: return
        val block: TagKey<Item> = getTagKey(name, BLOCK)
        // Shapeless Crafting
        HTShapelessRecipeJsonBuilder
            .create(ingot, 9)
            .input(block)
            .unlockedBy(block)
            .offerTo(exporter)
    }

    @JvmStatic
    private fun ingotToDustRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST] ?: return
        val ingot: TagKey<Item> = getTagKey(name, INGOT)
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTIngredientNew.ofItem(ingot),
            HTRecipeResultNew.ofItem(dust),
            recipeId = HTMachineRecipeJsonBuilders.createRecipeId(dust).withSuffixedPath("_from_ingot"),
        )
    }

    @JvmStatic
    private fun ingotToPlateRecipe(
        exporter: RecipeExporter,
        wrapper: (RecipeExporter, Boolean) -> RecipeExporter,
        name: String,
        properties: HTPropertyHolder,
    ) {
        val plate: ItemConvertible = properties[PLATE] ?: return
        val ingot: TagKey<Item> = getTagKey(name, INGOT)
        // Shaped Crafting (only hard mode)
        HTShapedRecipeJsonBuilder
            .create(plate)
            .patterns(
                "A",
                "B",
                "B",
            ).input('A', RagiumContents.Misc.FORGE_HAMMER)
            .input('B', ingot)
            .unlockedBy(ingot)
            .offerTo(wrapper(exporter, true))
        // Metal Former Recipe
        HTMachineRecipeJsonBuilders.createMetalFormer(
            exporter,
            HTItemIngredient.ofItem(ingot),
            HTItemResult.ofItem(plate),
        )
    }

    @JvmStatic
    private fun plateToDustRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST] ?: return
        val plate: TagKey<Item> = getTagKey(name, PLATE)
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTIngredientNew.ofItem(plate),
            HTRecipeResultNew.ofItem(dust),
            recipeId = HTMachineRecipeJsonBuilders.createRecipeId(dust).withSuffixedPath("_from_plate"),
        )
    }

    @JvmStatic
    private fun oreToRawRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        val rawMaterial: ItemConvertible = properties[RAW] ?: return
        val ore: TagKey<Item> = getTagKey(name, ORE)
        // Grinder Recipe
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTIngredientNew.ofItem(ore),
            HTRecipeResultNew.ofItem(rawMaterial, 2),
            properties[ORE_SUB_PRODUCTS]?.let { HTRecipeResultNew.ofItem(it) },
        )
    }

    @JvmStatic
    private fun rawToDustRecipe(exporter: RecipeExporter, name: String, properties: HTPropertyHolder) {
        val dust: ItemConvertible = properties[DUST] ?: return
        val rawMaterial: TagKey<Item> = getTagKey(name, RAW)
        // Grinder Recipe
        HTMachineRecipeJsonBuilders.createGrinder(
            exporter,
            HTIngredientNew.ofItem(rawMaterial),
            HTRecipeResultNew.ofItem(dust),
            HTRecipeResultNew.ofItem(dust),
        )
    }

    @JvmStatic
    private fun rawToIngotRecipe(
        exporter: RecipeExporter,
        wrapper: (RecipeExporter, TagKey<Item>) -> RecipeExporter,
        name: String,
        properties: HTPropertyHolder,
    ) {
        val ingot: ItemConvertible = properties[INGOT] ?: return
        val rawMaterial: TagKey<Item> = getTagKey(name, RAW)
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            rawMaterial,
            ingot,
            suffix = "_from_raw",
            wrapper = wrapper,
        )
    }

    @JvmStatic
    private fun dustToIngotRecipe(
        exporter: RecipeExporter,
        wrapper: (RecipeExporter, TagKey<Item>) -> RecipeExporter,
        name: String,
        properties: HTPropertyHolder,
    ) {
        val ingot: ItemConvertible = properties[INGOT] ?: return
        val dust: TagKey<Item> = getTagKey(name, DUST)
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            dust,
            ingot,
            suffix = "_from_dust",
            wrapper = wrapper,
        )
    }
}
