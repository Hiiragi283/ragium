package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipe
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.function.Consumer

object HTMachineConverters {
    //    Vanilla    //

    @JvmStatic
    fun fromCooking(holder: RecipeHolder<SmeltingRecipe>, provider: HolderLookup.Provider): RecipeHolder<HTMachineRecipe> {
        val recipe: AbstractCookingRecipe = holder.value
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.MULTI_SMELTER)
            .itemInput(recipe.ingredients[0], 64)
            .itemOutput(recipe.getResultItem(provider).item, 64)
            .export(holder.id.withSuffix("_from_smelting"))
    }

    @JvmStatic
    fun fromComposting(level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>>) {
        HTMachineRecipeProxy.default(RagiumRecipes.EXTRACTOR).getRecipes(level, consumer)
        level
            .registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .listElements()
            .forEach { holder: Holder.Reference<Item> ->
                val chance: Float = holder.getData(NeoForgeDataMaps.COMPOSTABLES)?.chance ?: return@forEach
                HTMachineRecipeBuilder
                    .create(RagiumRecipes.EXTRACTOR)
                    .itemInput(holder.value())
                    .catalyst(Items.COMPOSTER)
                    .fluidOutput(RagiumFluids.BIOMASS, (1000 * chance).toInt())
                    .exportSuffixed("_from_${holder.idOrThrow.path}")
                    .let(consumer::accept)
            }
    }

    @JvmStatic
    fun fromFuel(level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>>) {
        level
            .registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .listElements()
            .forEach { holder: Holder.Reference<Item> ->
                val fuel: Int = holder.getData(NeoForgeDataMaps.FURNACE_FUELS)?.burnTime ?: return@forEach
                var water: Int = fuel / 100
                if (water <= 0) {
                    water = 1
                }
                HTMachineRecipeBuilder
                    .create(RagiumRecipes.STEAM_BOILER)
                    .itemInput(holder.value())
                    .waterInput(water)
                    .fluidOutput(RagiumFluids.STEAM, water * 100)
                    .exportSuffixed("_from_${holder.idOrThrow.path}")
                    .let(consumer::accept)
            }
    }

    //    Material    //

    @JvmStatic
    fun compressorGear(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEAR, key) ?: return null
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItems.GEAR_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${key.name}_gear"))
    }

    @JvmStatic
    fun compressorGem(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEM, key) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .itemInput(HTTagPrefix.DUST, key)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${key.name}_gem"))
    }

    @JvmStatic
    fun compressorPlate(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.PLATE, key) ?: return null
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItems.PLATE_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${key.name}_plate"))
    }

    @JvmStatic
    fun compressorRod(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.ROD, key) ?: return null
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .itemInput(mainPrefix, key)
            .catalyst(RagiumItems.ROD_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${key.name}_rod"))
    }

    @JvmStatic
    fun grinderMainToDust(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, _: HTMaterialKey) = material
        val mainPrefix: HTTagPrefix = type.getMainPrefix() ?: return null
        return grinderToDust(material, registry, mainPrefix, 1)
    }

    @JvmStatic
    fun grinderGearToDust(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.GEAR, 4)

    @JvmStatic
    fun grinderPlateToDust(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.PLATE, 1)

    @JvmStatic
    fun grinderRawToDust(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.RAW_MATERIAL, 2)

    @JvmStatic
    private fun grinderToDust(
        material: HTTypedMaterial,
        registry: HTMaterialRegistry,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
    ): RecipeHolder<HTMachineRecipe>? {
        val (_: HTMaterialType, key: HTMaterialKey) = material
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.DUST, key) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(inputPrefix, key)
            .itemOutput(output.value(), baseCount)
            .export(RagiumAPI.id("${key.name}_dust_from${inputPrefix.serializedName}"))
    }

    @JvmStatic
    fun grinderOreToRaw(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return null
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, key) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(HTTagPrefix.ORE, key)
            .itemOutput(output.value(), 2)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.SLAG)
            .export(RagiumAPI.id("raw_${key.name}_2x"))
    }

    @JvmStatic
    fun chemicalOre3x(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 3, RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME / 10)

    @JvmStatic
    fun chemicalOre4x(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 4, RagiumFluids.BLAZE_ACID, FluidType.BUCKET_VOLUME / 5)

    @JvmStatic
    fun chemicalOre5x(material: HTTypedMaterial, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 5, RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME / 2)

    @JvmStatic
    fun chemicalOre(
        material: HTTypedMaterial,
        registry: HTMaterialRegistry,
        count: Int,
        chemical: RagiumFluids,
        fluidAmount: Int,
    ): RecipeHolder<HTMachineRecipe>? {
        val (type: HTMaterialType, key: HTMaterialKey) = material
        val rawPrefix: HTTagPrefix = type.getRawPrefix() ?: return null
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, key) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.ORE, key)
            .fluidInput(chemical, fluidAmount)
            .itemOutput(output.value(), count)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, fluidAmount)
            .export(RagiumAPI.id("raw_${key.name}_${count}x"))
    }
}
