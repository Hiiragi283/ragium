package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.getAllRecipes
import hiiragi283.ragium.api.extension.isSource
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.internal.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

object HTRecipeConverters {
    //    Compressor    //

    @JvmStatic
    fun compressor(consumer: (HTCompressorRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(HTRecipeTypes.COMPRESSOR).forEach(consumer)
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
            .itemInput(mainPrefix, key, 4)
            .catalyst(RagiumItemTags.GEAR_MOLDS)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorGem(material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (_, key: HTMaterialKey) = material
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
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.ROD, key, 2)
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
        recipeManager.getAllRecipes(HTRecipeTypes.GRINDER).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            grinderOreToDust(material, consumer)
            grinderMainToDust(material, consumer)
            grinderGearToDust(material, consumer)
            grinderPlateToDust(material, consumer)
            grinderRawToDust(material, consumer)
        }
    }

    @JvmStatic
    private fun grinderOreToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getOreResultPrefix() ?: return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        val output: HTItemOutput = HTItemOutput.of(rawPrefix, key, count * 2)
        if (!output.isValid(false)) return
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(output)
            .export(consumer)
    }

    @JvmStatic
    private fun grinderMainToDust(material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, _) = material
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
        grinderToDust(material, HTTagPrefix.RAW_MATERIAL, 3, consumer, 4)
    }

    @JvmStatic
    private fun grinderToDust(
        material: HTTypedMaterial,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
        consumer: (HTGrinderRecipe) -> Unit,
        inputCount: Int = 1,
    ) {
        val (_, key: HTMaterialKey) = material
        val output: HTItemOutput = HTItemOutput.of(HTTagPrefix.DUST, key, baseCount)
        if (!output.isValid(false)) return
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(inputPrefix, key, inputCount)
            .itemOutput(output)
            .export(consumer)
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(consumer: (HTInfuserRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(HTRecipeTypes.INFUSER).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            infuserOreToRaw(material, consumer)
        }

        // Bucket + Fluid -> Fluid Bucket
        val lookup: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return
        lookup
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder
                    .infuser()
                    .itemInput(Items.BUCKET)
                    .fluidInput(fluid)
                    .itemOutput(bucket)
                    .export(consumer)
            }
        // Oxidizables
        lookup
            .lookupOrThrow(Registries.BLOCK)
            .listElements()
            .forEach { holder: Holder.Reference<Block> ->
                val block: Block = holder.value()
                val input = ItemStack(block)
                if (input.isEmpty) return@forEach
                val block1: Block = holder.getData(NeoForgeDataMaps.OXIDIZABLES)?.nextOxidationStage ?: return@forEach
                val output: HTItemOutput = HTItemOutput.of(block1)
                if (!output.isValid(false)) return@forEach
                HTFluidOutputRecipeBuilder
                    .infuser()
                    .itemInput(block)
                    .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 100)
                    .itemOutput(output)
                    .export(consumer)
            }
    }

    @JvmStatic
    private fun infuserOreToRaw(material: HTTypedMaterial, consumer: (HTInfuserRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getOreResultPrefix() ?: return
        val output: HTItemOutput = HTItemOutput.of(rawPrefix, key)
        if (!output.isValid(false)) return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        // 3x
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
            .itemOutput(output.copyWithCount(count * 3))
            .export(consumer)
        // 4x
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 500)
            .itemOutput(output.copyWithCount(count * 4))
            .export(consumer)
    }

    //    Extractor    //

    @JvmStatic
    fun extractor(consumer: (HTExtractorRecipe) -> Unit) {
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getAllRecipes(HTRecipeTypes.EXTRACTOR).forEach(consumer)
        // Fluid Bucket -> Bucket + Fluid
        val lookup: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return
        lookup
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder
                    .extractor()
                    .itemInput(bucket)
                    .itemOutput(Items.BUCKET)
                    .fluidOutput(fluid)
                    .export(consumer)
            }
    }
}
