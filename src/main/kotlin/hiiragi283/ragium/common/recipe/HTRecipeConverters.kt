package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.getAllRecipes
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.internal.RagiumConfig
import net.minecraft.world.item.crafting.RecipeManager

object HTRecipeConverters {
    //    Compressor    //

    @JvmStatic
    fun compressor(consumer: (HTCompressorRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(RagiumRecipeTypes.COMPRESSOR.get()).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            compressorGear(material, consumer)
            compressorGem(material, consumer)
            compressorPlate(material, consumer)
            compressorRod(material, consumer)
            compressorWire(material, consumer)
        }
    }

    @JvmStatic
    private fun compressorGear(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.GEAR, key)
        if (!output.isValid(false)) return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.GEAR_MOLDS)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorGem(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.GEM, key)
        if (!output.isValid(false)) return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(HTTagPrefix.DUST, key)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorPlate(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.PLATE, key)
        if (!output.isValid(false)) return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.PLATE_MOLDS)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorRod(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.ROD, key)
        if (!output.isValid(false)) return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.ROD_MOLDS)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorWire(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.WIRE, key, 2)
        if (!output.isValid(false)) return
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.WIRE_MOLDS)
            .itemOutput(output)
            .export(consumer)
    }

    //    Grinder    //

    @JvmStatic
    fun grinder(consumer: (HTGrinderRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(RagiumRecipeTypes.GRINDER.get()).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            grinderOreToRaw(material, consumer)
            grinderMainToDust(material, consumer)
            grinderGearToDust(material, consumer)
            grinderPlateToDust(material, consumer)
            grinderRawToDust(material, consumer)
        }
    }

    @JvmStatic
    private fun grinderOreToRaw(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return
        val output: HTItemOutput = HTItemOutput.of(rawPrefix, key)
        if (!output.isValid(false)) return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(output.copyWithCount(count * 2))
            .export(consumer)
    }

    @JvmStatic
    private fun grinderMainToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, _: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        grinderToDust(material, mainPrefix, 1, consumer)
    }

    @JvmStatic
    private fun grinderGearToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, HTTagPrefix.GEAR, 4, consumer)
    }

    @JvmStatic
    private fun grinderPlateToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, HTTagPrefix.PLATE, 1, consumer)
    }

    @JvmStatic
    private fun grinderRawToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(material, HTTagPrefix.RAW_MATERIAL, 2, consumer)
    }

    @JvmStatic
    private fun grinderToDust(
        material: HTTypedMaterial,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
        consumer: (HTGrinderRecipe) -> Unit,
    ) {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.DUST, key)
        if (!output.isValid(false)) return
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(inputPrefix, key)
            .itemOutput(output.copyWithCount(baseCount))
            .export(consumer)
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(consumer: (HTInfuserRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(RagiumRecipeTypes.INFUSER.get()).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            infuserOreToRaw(material, consumer)
        }
    }

    @JvmStatic
    private fun infuserOreToRaw(material: HTTypedMaterial, consumer: (HTInfuserRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return
        val output: HTItemOutput = HTItemOutput.of(rawPrefix, key)
        if (!output.isValid(false)) return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        // 3x
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID, 500)
            .itemOutput(output.copyWithCount(count * 3))
            .export(consumer)
        // 4x
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID, 500)
            .itemOutput(output.copyWithCount(count * 4))
            .export(consumer)
    }
}
