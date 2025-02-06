package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.data.HTGrinderRecipeBuilder
import hiiragi283.ragium.api.data.HTInfuserRecipeBuilder
import hiiragi283.ragium.api.data.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager

object HTRecipeConverters {
    //    Compressor    //

    @JvmStatic
    fun compressor(recipeManager: RecipeManager, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        recipeManager
            .getAllRecipesFor(RagiumRecipeTypes.COMPRESSOR.get())
            .map(RecipeHolder<HTCompressorRecipe>::value)
            .forEach(consumer)
        registry.typedMaterials.forEach { material: HTTypedMaterial ->
            compressorGear(material, registry, consumer)
            compressorGem(material, registry, consumer)
            compressorPlate(material, registry, consumer)
            compressorRod(material, registry, consumer)
            compressorWire(material, registry, consumer)
        }
    }

    @JvmStatic
    private fun compressorGear(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEAR, key) ?: return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.GEAR_MOLDS)
            .itemOutput(output.value())
            .export(consumer)
    }

    @JvmStatic
    private fun compressorGem(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEM, key) ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(HTTagPrefix.DUST, key)
            .itemOutput(output.value())
            .export(consumer)
    }

    @JvmStatic
    private fun compressorPlate(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.PLATE, key) ?: return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.PLATE_MOLDS)
            .itemOutput(output.value())
            .export(consumer)
    }

    @JvmStatic
    private fun compressorRod(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.ROD, key) ?: return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.ROD_MOLDS)
            .itemOutput(output.value())
            .export(consumer)
    }

    @JvmStatic
    private fun compressorWire(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.WIRE, key) ?: return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.WIRE_MOLDS)
            .itemOutput(output.value(), 2)
            .export(consumer)
    }

    //    Grinder    //

    @JvmStatic
    fun grinder(recipeManager: RecipeManager, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        recipeManager
            .getAllRecipesFor(RagiumRecipeTypes.GRINDER.get())
            .map(RecipeHolder<HTGrinderRecipe>::value)
            .forEach(consumer)
        registry.typedMaterials.forEach { material: HTTypedMaterial ->
            grinderOreToRaw(material, registry, consumer)
            grinderMainToDust(material, registry, consumer)
            grinderGearToDust(material, registry, consumer)
            grinderPlateToDust(material, registry, consumer)
            grinderRawToDust(material, registry, consumer)
        }
    }

    @JvmStatic
    private fun grinderOreToRaw(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, key) ?: return
        val count: Int = registry.getProperty(key).getOrDefault(HTMaterialPropertyKeys.GRINDER_RAW_COUNT)
        HTGrinderRecipeBuilder()
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(output.value(), count * 2)
            .export(consumer)
    }

    @JvmStatic
    private fun grinderMainToDust(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, _: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        grinderToDust(material, registry, mainPrefix, 1, consumer)
    }

    @JvmStatic
    private fun grinderGearToDust(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, registry, HTTagPrefix.GEAR, 4, consumer)
    }

    @JvmStatic
    private fun grinderPlateToDust(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, registry, HTTagPrefix.PLATE, 1, consumer)
    }

    @JvmStatic
    private fun grinderRawToDust(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, registry, HTTagPrefix.RAW_MATERIAL, 2, consumer)
    }

    @JvmStatic
    private fun grinderToDust(
        material: HTTypedMaterial,
        registry: HTMaterialRegistry,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
        consumer: (HTGrinderRecipe) -> Unit,
    ) {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.DUST, key) ?: return
        HTGrinderRecipeBuilder()
            .itemInput(inputPrefix, key)
            .itemOutput(output.value(), baseCount)
            .export(consumer)
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(recipeManager: RecipeManager, registry: HTMaterialRegistry, consumer: (HTInfuserRecipe) -> Unit) {
        recipeManager
            .getAllRecipesFor(RagiumRecipeTypes.INFUSER.get())
            .map(RecipeHolder<HTInfuserRecipe>::value)
            .forEach(consumer)
        registry.typedMaterials.forEach { material: HTTypedMaterial ->
            infuserOreToRaw(material, registry, consumer)
        }
    }

    @JvmStatic
    private fun infuserOreToRaw(material: HTTypedMaterial, registry: HTMaterialRegistry, consumer: (HTInfuserRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, key) ?: return
        val count: Int = registry.getProperty(key).getOrDefault(HTMaterialPropertyKeys.GRINDER_RAW_COUNT)
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID)
            .itemOutput(output.value(), count * 3)
            .export(consumer)
    }
}
