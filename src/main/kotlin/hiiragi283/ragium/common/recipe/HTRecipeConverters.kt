package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.getValidRecipes
import hiiragi283.ragium.api.extension.isSource
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.internal.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
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
        val lookup: HolderGetter<Item> =
            RagiumAPI.getInstance().getCurrentLookup()?.lookupOrThrow(Registries.ITEM) ?: return
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getValidRecipes(HTRecipeTypes.COMPRESSOR).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            compressorGear(lookup, material, consumer)
            compressorGem(lookup, material, consumer)
            compressorPlate(lookup, material, consumer)
            compressorRod(lookup, material, consumer)
            compressorWire(lookup, material, consumer)
        }
    }

    @JvmStatic
    private fun compressorGear(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(mainPrefix, key, 4)
            .catalyst(RagiumItemTags.GEAR_MOLDS)
            .itemOutput(HTTagPrefix.GEAR, key)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorGem(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (_, key: HTMaterialKey) = material
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(HTTagPrefix.DUST, key)
            .itemOutput(HTTagPrefix.GEM, key)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorPlate(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.PLATE_MOLDS)
            .itemOutput(HTTagPrefix.PLATE, key)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorRod(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.ROD_MOLDS)
            .itemOutput(HTTagPrefix.ROD, key, 2)
            .export(consumer)
    }

    @JvmStatic
    private fun compressorWire(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTCompressorRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItemTags.WIRE_MOLDS)
            .itemOutput(HTTagPrefix.WIRE, key, 2)
            .export(consumer)
    }

    //    Grinder    //

    @JvmStatic
    fun grinder(consumer: (HTGrinderRecipe) -> Unit) {
        val lookup: HolderGetter<Item> =
            RagiumAPI.getInstance().getCurrentLookup()?.lookupOrThrow(Registries.ITEM) ?: return
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getValidRecipes(HTRecipeTypes.GRINDER).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            grinderOreToDust(lookup, material, consumer)
            grinderMainToDust(lookup, material, consumer)
            grinderGearToDust(lookup, material, consumer)
            grinderPlateToDust(lookup, material, consumer)
            grinderRawToDust(lookup, material, consumer)
        }
    }

    @JvmStatic
    private fun grinderOreToDust(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getOreResultPrefix() ?: return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(rawPrefix, key, count * 2)
            .export(consumer)
    }

    @JvmStatic
    private fun grinderMainToDust(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        val (type: HTMaterialType, _) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return
        grinderToDust(lookup, material, mainPrefix, 1, consumer)
    }

    @JvmStatic
    private fun grinderGearToDust(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(lookup, material, HTTagPrefix.GEAR, 4, consumer)
    }

    @JvmStatic
    private fun grinderPlateToDust(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(lookup, material, HTTagPrefix.PLATE, 1, consumer)
    }

    @JvmStatic
    private fun grinderRawToDust(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTGrinderRecipe) -> Unit) {
        grinderToDust(lookup, material, HTTagPrefix.RAW_MATERIAL, 3, consumer, 4)
    }

    @JvmStatic
    private fun grinderToDust(
        lookup: HolderGetter<Item>,
        material: HTTypedMaterial,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
        consumer: (HTGrinderRecipe) -> Unit,
        inputCount: Int = 1,
    ) {
        val (_, key: HTMaterialKey) = material
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(inputPrefix, key, inputCount)
            .itemOutput(HTTagPrefix.DUST, key, baseCount)
            .export(consumer)
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(consumer: (HTInfuserRecipe) -> Unit) {
        val itemGetter: HolderGetter<Item> =
            RagiumAPI.getInstance().getCurrentLookup()?.lookupOrThrow(Registries.ITEM) ?: return
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getValidRecipes(HTRecipeTypes.INFUSER).forEach(consumer)
        RagiumAPI.getInstance().getMaterialRegistry().typedMaterials.forEach { material: HTTypedMaterial ->
            infuserOreToRaw(itemGetter, material, consumer)
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
                    .infuser(itemGetter)
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
                HTFluidOutputRecipeBuilder
                    .infuser(itemGetter)
                    .itemInput(block)
                    .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 100)
                    .itemOutput(block1)
                    .export(consumer)
            }
    }

    @JvmStatic
    private fun infuserOreToRaw(lookup: HolderGetter<Item>, material: HTTypedMaterial, consumer: (HTInfuserRecipe) -> Unit) {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getOreResultPrefix() ?: return
        val count: Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1
        // 3x
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
            .itemOutput(rawPrefix, key, count * 3)
            .export(consumer)
        // 4x
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 500)
            .itemOutput(rawPrefix, key, count * 4)
            .export(consumer)
    }

    //    Extractor    //

    @JvmStatic
    fun extractor(consumer: (HTExtractorRecipe) -> Unit) {
        val lookup: HolderGetter<Item> =
            RagiumAPI.getInstance().getCurrentLookup()?.lookupOrThrow(Registries.ITEM) ?: return
        val recipeManager: RecipeManager = RagiumAPI.getInstance().getCurrentServer()?.recipeManager ?: return
        recipeManager.getValidRecipes(HTRecipeTypes.EXTRACTOR).forEach(consumer)
        // Fluid Bucket -> Bucket + Fluid
        val access: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return
        access
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder
                    .extractor(lookup)
                    .itemInput(bucket)
                    .itemOutput(Items.BUCKET)
                    .fluidOutput(fluid)
                    .export(consumer)
            }
    }
}
