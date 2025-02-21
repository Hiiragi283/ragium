package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.isSource
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
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
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI.Companion
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            if (mainPrefix != null) {
                // Gear
                HTSingleItemRecipeBuilder.Companion
                    .compressor(lookup)
                    .itemInput(mainPrefix, key, 4)
                    .catalyst(RagiumItemTags.GEAR_MOLDS)
                    .itemOutput(HTTagPrefix.GEAR, key)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_gear"), builder::add)
                // Plate
                HTSingleItemRecipeBuilder.Companion
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.PLATE_MOLDS)
                    .itemOutput(HTTagPrefix.PLATE, key)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_plate"), builder::add)
                // Rod
                HTSingleItemRecipeBuilder.Companion
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.ROD_MOLDS)
                    .itemOutput(HTTagPrefix.ROD, key, 2)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_rod"), builder::add)
                // Wire
                HTSingleItemRecipeBuilder.Companion
                    .compressor(lookup)
                    .itemInput(mainPrefix, key)
                    .catalyst(RagiumItemTags.WIRE_MOLDS)
                    .itemOutput(HTTagPrefix.WIRE, key, 2)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_wire"), builder::add)
            }
            // Gem
            HTSingleItemRecipeBuilder.Companion
                .compressor(lookup)
                .itemInput(HTTagPrefix.DUST, key)
                .itemOutput(HTTagPrefix.GEM, key)
                .exportNew(RagiumAPI.Companion.id("runtime_${name}_gem"), builder::add)
        }
        return builder
    }

    //    Grinder    //

    @JvmStatic
    fun grinder(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTGrinderRecipe>> {
        val builder: MutableList<RecipeHolder<HTGrinderRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.GRINDER).forEach(builder::add)
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI.Companion
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                val count: Int = RagiumAPI.Companion.getInstance().getGrinderOutputCount(key)
                HTSingleItemRecipeBuilder.Companion
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .itemOutput(resultPrefix, key, count * 2)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_dust_from_ore"), builder::add)
            }
            // Gem/Ingot
            if (mainPrefix != null) {
                HTSingleItemRecipeBuilder.Companion
                    .grinder(lookup)
                    .itemInput(mainPrefix, key)
                    .itemOutput(HTTagPrefix.DUST, key)
                    .exportNew(RagiumAPI.Companion.id("runtime_${name}_dust_from_main"), builder::add)
            }
            // Gear
            HTSingleItemRecipeBuilder.Companion
                .grinder(lookup)
                .itemInput(HTTagPrefix.GEAR, key)
                .itemOutput(HTTagPrefix.DUST, key, 4)
                .exportNew(RagiumAPI.Companion.id("runtime_${name}_dust_from_gear"), builder::add)
            // Plate
            HTSingleItemRecipeBuilder.Companion
                .grinder(lookup)
                .itemInput(HTTagPrefix.PLATE, key)
                .itemOutput(HTTagPrefix.DUST, key)
                .exportNew(RagiumAPI.Companion.id("runtime_${name}_dust_from_plate"), builder::add)
            // Raw
            HTSingleItemRecipeBuilder.Companion
                .grinder(lookup)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key, 4)
                .itemOutput(HTTagPrefix.DUST, key, 3)
                .exportNew(RagiumAPI.Companion.id("runtime_${name}_dust_from_raw"), builder::add)
        }
        return builder
    }

    //    Infuser    //

    @JvmStatic
    fun infuser(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTInfuserRecipe>> {
        val builder: MutableList<RecipeHolder<HTInfuserRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.INFUSER).forEach(builder::add)
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI.Companion
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val resultPrefix: HTTagPrefix = type.getOreResultPrefix() ?: continue
            val count: Int = RagiumAPI.Companion.getInstance().getGrinderOutputCount(key)
            // 3x
            HTFluidOutputRecipeBuilder.Companion
                .infuser(lookup)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluidTags.SULFURIC_ACID, 500)
                .itemOutput(resultPrefix, key, count * 3)
                .exportNew(RagiumAPI.Companion.id("runtime_3x_$name"), builder::add)
            // 4x
            HTFluidOutputRecipeBuilder.Companion
                .infuser(lookup)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluidTags.HYDROFLUORIC_ACID, 500)
                .itemOutput(resultPrefix, key, count * 4)
                .exportNew(RagiumAPI.Companion.id("runtime_4x_$name"), builder::add)
        }
        // Bucket + Fluid -> Fluid Bucket
        val access: RegistryAccess = RagiumAPI.Companion.getInstance().getCurrentLookup() ?: return builder
        access
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder.Companion
                    .infuser(lookup)
                    .itemInput(Items.BUCKET)
                    .fluidInput(fluid)
                    .itemOutput(bucket)
                    .exportNew(RagiumAPI.Companion.id("runtime_${holder.idOrThrow.path}_bucket"), builder::add)
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
                HTFluidOutputRecipeBuilder.Companion
                    .infuser(lookup)
                    .itemInput(block)
                    .fluidInput(RagiumFluidTags.OXYGEN, 100)
                    .itemOutput(block1)
                    .exportNew(RagiumAPI.Companion.id("runtime_${holder.idOrThrow.path}"), builder::add)
            }
        return builder
    }

    //    Extractor    //

    @JvmStatic
    fun extractor(lookup: HolderGetter<Item>, recipeManager: RecipeManager): List<RecipeHolder<HTExtractorRecipe>> {
        val builder: MutableList<RecipeHolder<HTExtractorRecipe>> = mutableListOf()
        recipeManager.getAllRecipesFor(HTRecipeTypes.EXTRACTOR).forEach(builder::add)
        // Fluid Bucket -> Bucket + Fluid
        val access: RegistryAccess = RagiumAPI.Companion.getInstance().getCurrentLookup() ?: return builder
        access
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                HTFluidOutputRecipeBuilder.Companion
                    .extractor(lookup)
                    .itemInput(bucket)
                    .itemOutput(Items.BUCKET)
                    .fluidOutput(fluid)
                    .exportNew(RagiumAPI.Companion.id("runtime_${holder.idOrThrow.path}"), builder::add)
            }
        return builder
    }
}
