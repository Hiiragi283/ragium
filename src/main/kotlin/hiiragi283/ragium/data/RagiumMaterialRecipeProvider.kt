package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.RagiumMaterials
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.tags.HTTagPrefixes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumMaterialRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    private fun tagValidator(exporter: RecipeExporter, vararg tagKey: TagKey<Item>): RecipeExporter =
        withConditions(exporter, ResourceConditions.tagsPopulated(*tagKey))

    override fun getName(): String = "Recipes/Material"

    override fun generate(exporter: RecipeExporter) {
        RagiumMaterials.entries.forEach { material: RagiumMaterials ->
            ingotToBlockRecipe(exporter, material)
            blockToIngotRecipe(exporter, material)

            ingotToPlateRecipe(exporter, material)

            ingotToDustRecipe(exporter, material)
            gemToDustRecipe(exporter, material)
            plateToDustRecipe(exporter, material)

            oreToRawRecipe(exporter, material)
            oreToGemRecipe(exporter, material)
            rawToDustRecipe(exporter, material)

            rawToIngotRecipe(exporter, material)
            dustToIngotRecipe(exporter, material)
        }
    }

    private fun ingotToBlockRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.INGOTS)) return
        val block: ItemConvertible = material.getBlock() ?: return
        val ingot: TagKey<Item> = HTTagPrefixes.INGOTS.createTag(material)
        // Shaped Crafting
        HTShapedRecipeJsonBuilder
            .create(block)
            .patterns(
                "AAA",
                "AAA",
                "AAA",
            ).input('A', ingot)
            .unlockedBy(ingot)
            .offerTo(tagValidator(exporter, ingot))
    }

    private fun blockToIngotRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.STORAGE_BLOCKS)) return
        val ingot: ItemConvertible = material.getIngot() ?: return
        val block: TagKey<Item> = HTTagPrefixes.STORAGE_BLOCKS.createTag(material)
        // Shapeless Crafting
        HTShapelessRecipeJsonBuilder
            .create(ingot, 9)
            .input(block)
            .unlockedBy(block)
            .offerTo(tagValidator(exporter, block))
    }

    private fun ingotToPlateRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.INGOTS) || !material.isValidPrefix(HTTagPrefixes.PLATES)) return
        val ingot: TagKey<Item> = HTTagPrefixes.INGOTS.createTag(material)
        val plate: TagKey<Item> = HTTagPrefixes.PLATES.createTag(material)
        // Metal Former Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.METAL_FORMER)
            .itemInput(ingot)
            .itemOutput(plate)
            .offerTo(tagValidator(exporter, ingot, plate), plate)
    }

    private fun ingotToDustRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.INGOTS) || !material.isValidPrefix(HTTagPrefixes.DUSTS)) return
        val ingot: TagKey<Item> = HTTagPrefixes.INGOTS.createTag(material)
        val dust: TagKey<Item> = HTTagPrefixes.DUSTS.createTag(material)
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(ingot)
            .itemOutput(dust)
            .offerTo(tagValidator(exporter, ingot, dust), dust, "_from_ingot")
    }

    private fun gemToDustRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.GEMS) || !material.isValidPrefix(HTTagPrefixes.DUSTS)) return
        val gem: TagKey<Item> = HTTagPrefixes.GEMS.createTag(material)
        val dust: TagKey<Item> = HTTagPrefixes.DUSTS.createTag(material)
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(gem)
            .itemOutput(dust)
            .offerTo(tagValidator(exporter, gem, dust), dust, "_from_gem")
    }

    private fun plateToDustRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.PLATES) || !material.isValidPrefix(HTTagPrefixes.DUSTS)) return
        val plate: TagKey<Item> = HTTagPrefixes.PLATES.createTag(material)
        val dust: TagKey<Item> = HTTagPrefixes.DUSTS.createTag(material)
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(plate)
            .itemOutput(dust)
            .offerTo(tagValidator(exporter, plate, dust), dust, "_from_plate")
    }

    private fun oreToRawRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.ORES) || !material.isValidPrefix(HTTagPrefixes.RAW_MATERIALS)) return
        val ore: TagKey<Item> = HTTagPrefixes.ORES.createTag(material)
        val rawMaterial: TagKey<Item> = HTTagPrefixes.RAW_MATERIALS.createTag(material)
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(ore)
            .itemOutput(rawMaterial, 2)
            .offerTo(tagValidator(exporter, ore, rawMaterial), rawMaterial)
    }

    private fun oreToGemRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.ORES) || !material.isValidPrefix(HTTagPrefixes.GEMS)) return
        val ore: TagKey<Item> = HTTagPrefixes.ORES.createTag(material)
        val gem: TagKey<Item> = HTTagPrefixes.GEMS.createTag(material)
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(ore)
            .itemOutput(gem, 2)
            .offerTo(tagValidator(exporter, ore, gem), gem)
    }

    private fun rawToDustRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.RAW_MATERIALS) || !material.isValidPrefix(HTTagPrefixes.DUSTS)) return
        val rawMaterial: TagKey<Item> = HTTagPrefixes.RAW_MATERIALS.createTag(material)
        val dust: TagKey<Item> = HTTagPrefixes.DUSTS.createTag(material)
        // Grinder Recipe
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineTypes.Processor.GRINDER)
            .itemInput(rawMaterial)
            .itemOutput(dust, 2)
            .offerTo(tagValidator(exporter, rawMaterial, dust), dust, "_from_raw")
    }

    private fun rawToIngotRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.RAW_MATERIALS)) return
        val rawMaterial: TagKey<Item> = HTTagPrefixes.RAW_MATERIALS.createTag(material)
        val result: ItemConvertible = material.getIngot() ?: material.getGem() ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            rawMaterial,
            result,
            suffix = "_from_raw",
            wrapper = ::tagValidator,
        )
    }

    private fun dustToIngotRecipe(exporter: RecipeExporter, material: RagiumMaterials) {
        if (!material.isValidPrefix(HTTagPrefixes.RAW_MATERIALS)) return
        val dust: TagKey<Item> = HTTagPrefixes.DUSTS.createTag(material)
        val result: ItemConvertible = material.getIngot() ?: material.getGem() ?: return
        HTCookingRecipeJsonBuilder.smeltAndBlast(
            exporter,
            dust,
            result,
            suffix = "_from_dust",
            wrapper = ::tagValidator,
        )
    }
}
