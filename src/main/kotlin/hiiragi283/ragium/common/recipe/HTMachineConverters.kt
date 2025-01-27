package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.neoforged.neoforge.fluids.FluidType

object HTMachineConverters {
    //    Vanilla    //

    @JvmStatic
    fun fromCooking(holder: RecipeHolder<SmeltingRecipe>, provider: HolderLookup.Provider): RecipeHolder<HTMachineRecipe> {
        val recipe: AbstractCookingRecipe = holder.value
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MULTI_SMELTER)
            .itemInput(recipe.ingredients[0])
            .itemOutput(recipe.getResultItem(provider))
            .export(holder.id.withSuffix("_from_smelting"))
    }

    @JvmStatic
    fun fromCutting(holder: RecipeHolder<StonecutterRecipe>, provider: HolderLookup.Provider): RecipeHolder<HTMachineRecipe> {
        val recipe: StonecutterRecipe = holder.value
        val output: ItemStack = recipe.getResultItem(provider)
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CUTTING_MACHINE)
            .itemInput(recipe.ingredients[0])
            .catalyst(output.item)
            .itemOutput(recipe.getResultItem(provider))
            .export(holder.id.withSuffix("_from_cutting"))
    }

    //    Material    //

    @JvmStatic
    fun compressorGear(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEAR, material) ?: return null
        val mainPrefix: HTTagPrefix = registry.getType(material).getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(mainPrefix, material)
            .catalyst(RagiumItems.GEAR_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${material.name}_gear"))
    }

    @JvmStatic
    fun compressorGem(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.GEM, material) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(HTTagPrefix.DUST, material)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${material.name}_gem"))
    }

    @JvmStatic
    fun compressorPlate(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.PLATE, material) ?: return null
        val mainPrefix: HTTagPrefix = registry.getType(material).getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(mainPrefix, material)
            .catalyst(RagiumItems.PLATE_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${material.name}_plate"))
    }

    @JvmStatic
    fun compressorRod(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.ROD, material) ?: return null
        val mainPrefix: HTTagPrefix = registry.getType(material).getMainPrefix() ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(mainPrefix, material)
            .catalyst(RagiumItems.ROD_PRESS_MOLD)
            .itemOutput(output.value())
            .export(RagiumAPI.id("${material.name}_rod"))
    }

    @JvmStatic
    fun grinderMainToDust(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val mainPrefix: HTTagPrefix = registry.getType(material).getMainPrefix() ?: return null
        return grinderToDust(material, registry, mainPrefix, 1)
    }

    @JvmStatic
    fun grinderGearToDust(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.GEAR, 4)

    @JvmStatic
    fun grinderPlateToDust(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.PLATE, 1)

    @JvmStatic
    fun grinderRawToDust(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        grinderToDust(material, registry, HTTagPrefix.RAW_MATERIAL, 2)

    @JvmStatic
    private fun grinderToDust(
        material: HTMaterialKey,
        registry: HTMaterialRegistry,
        inputPrefix: HTTagPrefix,
        baseCount: Int,
    ): RecipeHolder<HTMachineRecipe>? {
        val output: Holder<Item> = registry.getFirstItem(HTTagPrefix.DUST, material) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(inputPrefix, material)
            .itemOutput(output.value(), baseCount)
            .export(RagiumAPI.id("${material.name}_dust_from${inputPrefix.serializedName}"))
    }

    @JvmStatic
    fun grinderOreToRaw(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? {
        val rawPrefix: HTTagPrefix = registry.getType(material).getRawPrefix() ?: return null
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, material) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.ORE, material)
            .itemOutput(output.value(), 2)
            .itemOutput(RagiumItems.SLAG)
            .export(RagiumAPI.id("raw_${material.name}_2x"))
    }

    @JvmStatic
    fun chemicalOre3x(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 3, RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME / 10)

    @JvmStatic
    fun chemicalOre4x(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 4, RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME / 5)

    @JvmStatic
    fun chemicalOre5x(material: HTMaterialKey, registry: HTMaterialRegistry): RecipeHolder<HTMachineRecipe>? =
        chemicalOre(material, registry, 5, RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME / 2)

    @JvmStatic
    fun chemicalOre(
        material: HTMaterialKey,
        registry: HTMaterialRegistry,
        count: Int,
        chemical: RagiumFluids,
        fluidAmount: Int,
    ): RecipeHolder<HTMachineRecipe>? {
        val rawPrefix: HTTagPrefix = registry.getType(material).getRawPrefix() ?: return null
        val output: Holder<Item> = registry.getFirstItem(rawPrefix, material) ?: return null
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(chemical, fluidAmount)
            .itemOutput(output.value(), count)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, fluidAmount)
            .export(RagiumAPI.id("raw_${material.name}_${count}x"))
    }
}
