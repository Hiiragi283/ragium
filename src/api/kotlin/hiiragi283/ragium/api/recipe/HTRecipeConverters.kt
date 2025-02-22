package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSolidifierRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.isSource
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.base.HTMoltenFluidIngredient
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

object HTRecipeConverters {
    //    Compressor    //

    @JvmStatic
    fun compressor(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTCompressorRecipe>> {
        val builder: MutableList<RecipeHolder<HTCompressorRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.COMPRESSOR).forEach(builder::add)
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            if (mainPrefix != null) {
                // Gear
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(mainPrefix, key, 4)
                    .catalyst(RagiumItemTags.GEAR_MOLDS)
                    .itemOutput(HTTagPrefix.GEAR, key)
                    .export(RagiumAPI.id("runtime_${name}_gear"), builder::add)
                // Plate
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.PLATE_MOLDS)
                    .itemOutput(HTTagPrefix.PLATE, key)
                    .export(RagiumAPI.id("runtime_${name}_plate"), builder::add)
                // Rod
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.ROD_MOLDS)
                    .itemOutput(HTTagPrefix.ROD, key, 2)
                    .export(RagiumAPI.id("runtime_${name}_rod"), builder::add)
                // Wire
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.WIRE_MOLDS)
                    .itemOutput(HTTagPrefix.WIRE, key, 2)
                    .export(RagiumAPI.id("runtime_${name}_wire"), builder::add)
            }
            // Gem
            HTSingleItemRecipeBuilder
                .compressor(lookup)
                .itemInput(HTTagPrefix.DUST, key)
                .itemOutput(HTTagPrefix.GEM, key)
                .export(RagiumAPI.id("runtime_${name}_gem"), builder::add)
        }
        return builder
    }

    //    Grinder    //

    @JvmStatic
    fun grinder(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTGrinderRecipe>> {
        val builder: MutableList<RecipeHolder<HTGrinderRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.GRINDER).forEach(builder::add)
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .itemOutput(resultPrefix, key, count * 2)
                    .export(RagiumAPI.id("runtime_${name}_dust_from_ore"), builder::add)
            }
            // Gem/Ingot
            if (mainPrefix != null) {
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(mainPrefix, key)
                    .itemOutput(HTTagPrefix.DUST, key)
                    .export(RagiumAPI.id("runtime_${name}_dust_from_main"), builder::add)
            }
            // Gear
            HTSingleItemRecipeBuilder
                .grinder(lookup)
                .itemInput(HTTagPrefix.GEAR, key)
                .itemOutput(HTTagPrefix.DUST, key, 4)
                .export(RagiumAPI.id("runtime_${name}_dust_from_gear"), builder::add)
            // Plate
            HTSingleItemRecipeBuilder
                .grinder(lookup)
                .itemInput(HTTagPrefix.PLATE, key)
                .itemOutput(HTTagPrefix.DUST, key)
                .export(RagiumAPI.id("runtime_${name}_dust_from_plate"), builder::add)
            // Raw
            HTSingleItemRecipeBuilder
                .grinder(lookup)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key, 4)
                .itemOutput(HTTagPrefix.DUST, key, 3)
                .export(RagiumAPI.id("runtime_${name}_dust_from_raw"), builder::add)
        }
        return builder
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTInfuserRecipe>> {
        val builder: MutableList<RecipeHolder<HTInfuserRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.INFUSER).forEach(builder::add)
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val resultPrefix: HTTagPrefix = type.getOreResultPrefix() ?: continue
            val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
            // 3x
            HTFluidOutputRecipeBuilder
                .infuser(lookup)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluidTags.SULFURIC_ACID, 500)
                .itemOutput(resultPrefix, key, count * 3)
                .export(RagiumAPI.id("runtime_3x_$name"), builder::add)
            // 4x
            HTFluidOutputRecipeBuilder
                .infuser(lookup)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluidTags.HYDROFLUORIC_ACID, 500)
                .itemOutput(resultPrefix, key, count * 4)
                .export(RagiumAPI.id("runtime_4x_$name"), builder::add)
        }
        // Bucket + Fluid -> Fluid Bucket
        val access: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return builder
        access
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(Items.BUCKET)
                    .fluidInput(fluid)
                    .itemOutput(bucket)
                    .export(RagiumAPI.id("runtime_${holder.idOrThrow.path}_bucket"), builder::add)
            }
        // Oxidizables
        access
            .lookupOrThrow(Registries.BLOCK)
            .listElements()
            .forEach { holder: Holder.Reference<Block> ->
                val block: Block = holder.value()
                val input = ItemStack(block)
                if (input.isEmpty) return@forEach
                val block1: Block = holder.getData(NeoForgeDataMaps.OXIDIZABLES)?.nextOxidationStage ?: return@forEach
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(block)
                    .fluidInput(RagiumFluidTags.OXYGEN, 100)
                    .itemOutput(block1)
                    .export(RagiumAPI.id("runtime_${holder.idOrThrow.path}"), builder::add)
            }
        return builder
    }

    //    Extractor    //

    @JvmStatic
    fun extractor(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTExtractorRecipe>> {
        val builder: MutableList<RecipeHolder<HTExtractorRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.EXTRACTOR).forEach(builder::add)
        // XX -> Molten Metal
        for (material: HTTypedMaterial in RagiumAPI.getInstance().getMaterialRegistry().typedMaterials) {
            val (type: HTMaterialType, key: HTMaterialKey) = material
            if (type.getMainPrefix() == HTTagPrefix.INGOT) {
                // Dust
                HTFluidOutputRecipeBuilder
                    .extractor(lookup)
                    .itemInput(HTTagPrefix.DUST, key)
                    .fluidOutput(RagiumAPI.getInstance().createMoltenMetalStack(key, RagiumAPI.INGOT_AMOUNT))
                    .export(RagiumAPI.id("runtime_molten_${key.name}_dust"), builder::add)
                // Ingot
                HTFluidOutputRecipeBuilder
                    .extractor(lookup)
                    .itemInput(HTTagPrefix.INGOT, key)
                    .fluidOutput(RagiumAPI.getInstance().createMoltenMetalStack(key, RagiumAPI.INGOT_AMOUNT))
                    .export(RagiumAPI.id("runtime_molten_${key.name}_ingot"), builder::add)
                // Nugget
                HTFluidOutputRecipeBuilder
                    .extractor(lookup)
                    .itemInput(HTTagPrefix.NUGGET, key)
                    .fluidOutput(RagiumAPI.getInstance().createMoltenMetalStack(key, RagiumAPI.INGOT_AMOUNT / 9))
                    .export(RagiumAPI.id("runtime_molten_${key.name}_nugget"), builder::add)
            }
        }
        // Fluid Bucket -> Bucket + Fluid
        val access: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return builder
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
                    .export(RagiumAPI.id("runtime_${holder.idOrThrow.path}"), builder::add)
            }
        return builder
    }

    //    Solidifier    //

    @JvmStatic
    fun solidifier(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTSolidifierRecipe>> {
        val builder: MutableList<RecipeHolder<HTSolidifierRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.SOLIDIFIER).forEach(builder::add)
        // Molten Metal -> XX
        for (material: HTTypedMaterial in RagiumAPI.getInstance().getMaterialRegistry().typedMaterials) {
            val (type: HTMaterialType, key: HTMaterialKey) = material
            if (type.getMainPrefix() == HTTagPrefix.INGOT) {
                // Gear
                HTSolidifierRecipeBuilder(lookup)
                    .fluidInput(HTMoltenFluidIngredient(key), RagiumAPI.INGOT_AMOUNT * 4)
                    .catalyst(RagiumItemTags.GEAR_MOLDS)
                    .itemOutput(HTTagPrefix.GEAR, key)
                    .export(RagiumAPI.id("runtime_molten_${key.name}_gear"), builder::add)
                // Plate
                HTSolidifierRecipeBuilder(lookup)
                    .fluidInput(HTMoltenFluidIngredient(key), RagiumAPI.INGOT_AMOUNT)
                    .catalyst(RagiumItemTags.PLATE_MOLDS)
                    .itemOutput(HTTagPrefix.PLATE, key)
                    .export(RagiumAPI.id("runtime_molten_${key.name}_plate"), builder::add)
                // Rod
                HTSolidifierRecipeBuilder(lookup)
                    .fluidInput(HTMoltenFluidIngredient(key), RagiumAPI.INGOT_AMOUNT / 2)
                    .catalyst(RagiumItemTags.ROD_MOLDS)
                    .itemOutput(HTTagPrefix.ROD, key)
                    .export(RagiumAPI.id("runtime_molten_${key.name}_rod"), builder::add)
                // Wire
                HTSolidifierRecipeBuilder(lookup)
                    .fluidInput(HTMoltenFluidIngredient(key), RagiumAPI.INGOT_AMOUNT / 2)
                    .catalyst(RagiumItemTags.WIRE_MOLDS)
                    .itemOutput(HTTagPrefix.WIRE, key)
                    .export(RagiumAPI.id("runtime_molten_${key.name}_wire"), builder::add)
            }
        }
        return builder
    }
}
