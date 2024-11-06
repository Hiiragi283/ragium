package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

@Suppress("DEPRECATION")
object RagiumMaterialRecipeProvider {
    @JvmStatic
    fun generate(exporter: RecipeExporter) {
        RagiumAPI.log { info("Registering Runtime Material Recipes...") }
        RagiumAPI.getInstance().materialRegistry.entryMap.forEach { (key: HTMaterialKey, entry: HTMaterialRegistry.Entry) ->
            val type: HTMaterialKey.Type = entry.type
            ingotToBlockRecipe(exporter, key, type)
            blockToIngotRecipe(exporter, key, type)

            ingotToPlateRecipe(exporter, key, type)

            ingotToDustRecipe(exporter, key, type)
            gemToDustRecipe(exporter, key, type)
            plateToDustRecipe(exporter, key, type)

            oreToRawRecipe(exporter, key, type)
            oreToGemRecipe(exporter, key, type)
            rawToDustRecipe(exporter, key, type)

            rawToIngotRecipe(exporter, key, type)
            dustToIngotRecipe(exporter, key, type)
        }
        RagiumAPI.log { info("Registered Runtime Material Recipes!") }
    }

    @Suppress("UnstableApiUsage")
    @JvmStatic
    private fun isPopulated(tagKey: TagKey<Item>): Boolean =
        ResourceConditionsImpl.tagsPopulated(RegistryKeys.ITEM.value, listOf(tagKey.id))

    @JvmStatic
    private fun ingotToBlockRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.INGOT)) return
        val block: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.STORAGE_BLOCK) ?: return
        val ingot: TagKey<Item> = HTTagPrefix.INGOT.createTag(key)
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
    private fun blockToIngotRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.STORAGE_BLOCK)) return
        val ingot: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT) ?: return
        val block: TagKey<Item> = HTTagPrefix.STORAGE_BLOCK.createTag(key)
        // Shapeless Crafting
        HTShapelessRecipeJsonBuilder
            .create(ingot, 9)
            .input(block)
            .unlockedBy(block)
            .offerTo(exporter)
    }

    @JvmStatic
    private fun ingotToPlateRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.INGOT) || !type.isValidPrefix(HTTagPrefix.PLATE)) return
        val ingot: TagKey<Item> = HTTagPrefix.INGOT.createTag(key)
        val plate: TagKey<Item> = HTTagPrefix.PLATE.createTag(key)
        if (!isPopulated(plate)) return
        // Metal Former Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.METAL_FORMER)
            .itemInput(ingot)
            .itemOutput(plate)
            .offerTo(exporter, plate)
    }

    @JvmStatic
    private fun ingotToDustRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.INGOT) || !type.isValidPrefix(HTTagPrefix.DUST)) return
        val ingot: TagKey<Item> = HTTagPrefix.INGOT.createTag(key)
        val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
        if (!isPopulated(dust)) return
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ingot)
            .itemOutput(dust)
            .offerTo(exporter, dust, "_from_ingot")
    }

    @JvmStatic
    private fun gemToDustRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.GEM) || !type.isValidPrefix(HTTagPrefix.DUST)) return
        val gem: TagKey<Item> = HTTagPrefix.GEM.createTag(key)
        val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
        if (!isPopulated(dust)) return
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(gem)
            .itemOutput(dust)
            .offerTo(exporter, dust, "_from_gem")
    }

    @JvmStatic
    private fun plateToDustRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.PLATE) || !type.isValidPrefix(HTTagPrefix.DUST)) return
        val plate: TagKey<Item> = HTTagPrefix.PLATE.createTag(key)
        val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
        if (!isPopulated(dust)) return
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(plate)
            .itemOutput(dust)
            .offerTo(exporter, dust, "_from_plate")
    }

    @JvmStatic
    private fun oreToRawRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.ORE) || !type.isValidPrefix(HTTagPrefix.RAW_MATERIAL)) return
        val ore: TagKey<Item> = HTTagPrefix.ORE.createTag(key)
        val rawMaterial: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
        if (!isPopulated(rawMaterial)) return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ore)
            .itemOutput(rawMaterial, 2)
            .offerTo(exporter, rawMaterial)
    }

    @JvmStatic
    private fun oreToGemRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.ORE) || !type.isValidPrefix(HTTagPrefix.GEM)) return
        val ore: TagKey<Item> = HTTagPrefix.ORE.createTag(key)
        val gem: TagKey<Item> = HTTagPrefix.GEM.createTag(key)
        if (!isPopulated(gem)) return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(ore)
            .itemOutput(gem, 2)
            .offerTo(exporter, gem)
    }

    @JvmStatic
    private fun rawToDustRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.RAW_MATERIAL) || !type.isValidPrefix(HTTagPrefix.DUST)) return
        val rawMaterial: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
        val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
        if (!isPopulated(dust)) return
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(rawMaterial)
            .itemOutput(dust, 2)
            .offerTo(exporter, dust, "_from_raw")
    }

    @JvmStatic
    private fun rawToIngotRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.RAW_MATERIAL)) return
        val rawMaterial: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
        if (!isPopulated(rawMaterial)) return
        val result: ItemConvertible =
            key.entry.getFirstItem(HTTagPrefix.INGOT) ?: key.entry.getFirstItem(HTTagPrefix.GEM) ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            rawMaterial,
            result,
            suffix = "_from_raw",
        )
    }

    @JvmStatic
    private fun dustToIngotRecipe(exporter: RecipeExporter, key: HTMaterialKey, type: HTMaterialKey.Type) {
        if (!type.isValidPrefix(HTTagPrefix.DUST)) return
        val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
        if (!isPopulated(dust)) return
        val result: ItemConvertible =
            key.entry.getFirstItem(HTTagPrefix.INGOT) ?: key.entry.getFirstItem(HTTagPrefix.GEM) ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            dust,
            result,
            suffix = "_from_dust",
        )
    }
}
