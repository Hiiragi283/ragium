package hiiragi283.ragium.data.server.recipe

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemHolder
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.api.util.collection.HTWrappedTable
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.requires
import hiiragi283.ragium.data.savePrefixed
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.integration.mek.RagiumEvilIntegration
import hiiragi283.ragium.integration.mek.RagiumMekIntegration
import mekanism.common.registration.impl.BlockRegistryObject
import mekanism.common.registration.impl.ItemRegistryObject
import mekanism.common.registries.MekanismBlocks
import mekanism.common.registries.MekanismItems
import mekanism.common.resource.IResource
import mekanism.common.resource.PrimaryResource
import mekanism.common.resource.ResourceType
import mekanism.common.resource.ore.OreBlockType
import mekanism.common.resource.ore.OreType
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredItem
import org.slf4j.Logger

object HTMaterialRecipeProvider : RagiumRecipeProvider.Child {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    data class SizedHolder(val holder: DeferredItem<*>, val count: Int = 1) : ItemLike by holder {
        val condition: ModLoadedCondition? = ModLoadedCondition(holder.id.namespace)
            .takeUnless { it.modid == "minecraft" || it.modid == RagiumAPI.MOD_ID }
    }

    @JvmStatic
    private val MATERIAL_ITEM_TABLE: HTTable.Mutable<HTTagPrefix, HTMaterialKey, SizedHolder> = mutableTableOf()

    @JvmStatic
    private fun putHolder(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        item: ItemLike,
        count: Int = 1,
    ) {
        if (MATERIAL_ITEM_TABLE.contains(prefix, material)) return
        if (prefix == HTTagPrefix.STORAGE_BLOCK && material.name.startsWith("raw_")) {
            putHolder(HTTagPrefix.RAW_STORAGE, HTMaterialKey.of(material.name.removePrefix("raw_")), item, count)
            return
        }
        val holder: DeferredItem<*> = when (item) {
            is DeferredItem<*> -> item
            is HTItemContent -> DeferredItem.createItem<Item>(item.key)

            else -> {
                val resourceKey: ResourceKey<Item> = item.asHolder().unwrapKey().orElseThrow()
                DeferredItem.createItem<Item>(resourceKey)
            }
        }
        MATERIAL_ITEM_TABLE.put(prefix, material, SizedHolder(holder, count))
        LOGGER.debug("Added prefix: $prefix, material: $material, item: ${holder.id}, count: $count")
    }

    @JvmStatic
    private fun initTable() {
        // Ragium
        RagiumItems.MATERIALS.forEach { content: HTItemContent.Material ->
            putHolder(content.tagPrefix, content.material, content)
        }
        // Vanilla
        putHolder(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        putHolder(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        putHolder(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI, 4)
        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)
        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items.QUARTZ, 2)
        // Mekanism
        MekanismBlocks.PROCESSED_RESOURCE_BLOCKS.forEach { (resource: IResource, obj: BlockRegistryObject<*, *>) ->
            putHolder(HTTagPrefix.STORAGE_BLOCK, HTMaterialKey.of(resource.registrySuffix), obj)
        }

        HTWrappedTable(
            MekanismItems.PROCESSED_RESOURCES,
        ).forEach { (type: ResourceType, resource: PrimaryResource, obj: ItemRegistryObject<Item>) ->
            val prefix: HTTagPrefix = HTTagPrefix.fromCommonName(type.baseTagPath) ?: return@forEach
            putHolder(prefix, HTMaterialKey.of(resource.registrySuffix), obj)
        }

        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.COAL, MekanismItems.COAL_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.DIAMOND, MekanismItems.DIAMOND_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.EMERALD, MekanismItems.EMERALD_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.LAPIS, MekanismItems.LAPIS_LAZULI_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.NETHERITE, MekanismItems.NETHERITE_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.QUARTZ, MekanismItems.QUARTZ_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.SALT, MekanismItems.SALT)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMaterialKeys.SULFUR, MekanismItems.SULFUR_DUST)
        putHolder(HTTagPrefix.DUST, RagiumMekIntegration.REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_DUST)

        putHolder(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE, MekanismItems.FLUORITE_GEM)

        putHolder(HTTagPrefix.INGOT, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_INGOT)
        putHolder(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_INGOT)
        putHolder(HTTagPrefix.INGOT, RagiumMekIntegration.REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_INGOT)
        putHolder(HTTagPrefix.INGOT, RagiumMekIntegration.REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_INGOT)

        putHolder(HTTagPrefix.NUGGET, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_NUGGET)
        putHolder(HTTagPrefix.NUGGET, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_NUGGET)
        putHolder(HTTagPrefix.NUGGET, RagiumMekIntegration.REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_NUGGET)
        putHolder(HTTagPrefix.NUGGET, RagiumMekIntegration.REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_NUGGET)

        MekanismBlocks.ORES.forEach { (type: OreType, ore: OreBlockType) ->
            putHolder(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.stone)
            putHolder(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.deepslate)
        }
        // Evil Craft
        putHolder(HTTagPrefix.DUST, RagiumEvilIntegration.DARK_GEM, itemHolder("evilcraft:dark_gem_crushed"))

        putHolder(HTTagPrefix.GEM, RagiumEvilIntegration.DARK_GEM, itemHolder("evilcraft:dark_gem"))

        putHolder(HTTagPrefix.ORE, RagiumEvilIntegration.DARK_GEM, itemHolder("evilcraft:dark_ore"))
        putHolder(HTTagPrefix.ORE, RagiumEvilIntegration.DARK_GEM, itemHolder("evilcraft:dark_ore_deepslate"))

        putHolder(HTTagPrefix.STORAGE_BLOCK, RagiumEvilIntegration.DARK_GEM, itemHolder("evilcraft:dark_block"))
    }

    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        initTable()
        // Ingot/Gem -> Block
        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', storage.parentPrefix, storage.material)
                .unlockedBy("has_input", has(storage.parentPrefix, storage.material))
                .savePrefixed(output)
        }
        // Block -> Ingot
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, ingot.material)
                .unlockedBy("has_ingot", has(HTTagPrefix.STORAGE_BLOCK, ingot.material))
                .savePrefixed(output)
        }
        // Block -> Gem
        RagiumItems.RawResources.entries.forEach { resource: RagiumItems.RawResources ->
            if (resource.tagPrefix != HTTagPrefix.GEM) return@forEach
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, resource, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, resource.material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, resource.material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems.Gears.entries.forEach { gear: RagiumItems.Gears ->
            val material: HTMaterialKey = gear.material
            val parentPrefix: HTTagPrefix = gear.parentPrefix
            // Shaped Recipe
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, gear)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', parentPrefix, material)
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_input", has(parentPrefix, material))
                .savePrefixed(output)
            // Compressor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(parentPrefix, material, 4)
                .catalyst(RagiumItems.GEAR_PRESS_MOLD)
                .itemOutput(gear)
                .save(output)
        }

        // Ingot/Gem -> Rod
        RagiumItems.Rods.entries.forEach { rod: RagiumItems.Rods ->
            val material: HTMaterialKey = rod.material
            val parentPrefix: HTTagPrefix = rod.parentPrefix
            // Shaped Recipe
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, rod, 2)
                .pattern("AB")
                .pattern("A ")
                .define('A', parentPrefix, material)
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_input", has(parentPrefix, material))
                .savePrefixed(output)
            // Compressor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(parentPrefix, material)
                .catalyst(RagiumItems.ROD_PRESS_MOLD)
                .itemOutput(rod, 2)
                .save(output)
        }

        MATERIAL_ITEM_TABLE.row(HTTagPrefix.DUST).forEach { (key: HTMaterialKey, dust: SizedHolder) ->
            registerToDust(output, key, dust)
        }
        MATERIAL_ITEM_TABLE.row(HTTagPrefix.GEM).forEach { (key: HTMaterialKey, gem: SizedHolder) ->
            registerOreToRaw(output, key, gem)
            // Compressor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(HTTagPrefix.DUST, key)
                .itemOutput(gem)
                .conditions(listOfNotNull(gem.condition))
                .saveSuffixed(output, "_from_dust")
        }
        MATERIAL_ITEM_TABLE.row(HTTagPrefix.INGOT).forEach { (key: HTMaterialKey, ingot: SizedHolder) ->
        }
        MATERIAL_ITEM_TABLE.row(HTTagPrefix.RAW_MATERIAL).forEach { (key: HTMaterialKey, raw: SizedHolder) ->
            registerOreToRaw(output, key, raw)
        }

        /*MATERIAL_ITEM_TABLE.row(HTTagPrefix.CLUMP).forEach { (key: HTMaterialKey, clump: SizedHolder) ->
            // Chemical Reactor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key)
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(clump, 2)
                .conditions(listOfNotNull(clump.condition))
                .saveSuffixed(output, "_from_raw")

            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
                .itemInput(HTTagPrefix.SHARD, key)
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(clump)
                .conditions(listOfNotNull(clump.condition))
                .saveSuffixed(output, "_from_shard")
        }
        MATERIAL_ITEM_TABLE.row(HTTagPrefix.SHARD).forEach { (key: HTMaterialKey, shard: SizedHolder) ->
            // Chemical Reactor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key, 3)
                .fluidInput(RagiumFluids.HYDROGEN_CHLORIDE, 200)
                .itemOutput(shard, 8)
                .conditions(listOfNotNull(shard.condition))
                .saveSuffixed(output, "_from_raw")

            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
                .itemInput(HTTagPrefix.CRYSTAL, key)
                .fluidInput(RagiumFluids.HYDROGEN_CHLORIDE, 200)
                .itemOutput(shard)
                .conditions(listOfNotNull(shard.condition))
                .saveSuffixed(output, "_from_crystal")
        }
        MATERIAL_ITEM_TABLE.row(HTTagPrefix.CRYSTAL).forEach { (key: HTMaterialKey, crystal: SizedHolder) ->
            // Chemical Reactor
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ULTIMATE)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key, 3)
                .fluidInput(RagiumFluids.SULFURIC_ACID, 100)
                .itemOutput(crystal, 10)
                .conditions(listOfNotNull(crystal.condition))
                .save(output)
        }*/
    }

    private fun registerOreToRaw(
        output: RecipeOutput,
        material: HTMaterialKey,
        holder: SizedHolder,
        subProduct: ItemLike? = null,
    ) {
        val baseCount: Int = holder.count
        // 2x Grinder
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(HTTagPrefix.ORE, material)
            .itemOutput(holder, baseCount * 2)
            .apply { subProduct?.let(this::itemOutput) }
            .itemOutput(RagiumItems.SLAG)
            .conditions(listOfNotNull(holder.condition))
            .saveSuffixed(output, "_2x")
        // 3x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidType.BUCKET_VOLUME / 10)
            .itemOutput(holder, baseCount * 3)
            .apply { subProduct?.let(this::itemOutput) }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 10)
            .conditions(listOfNotNull(holder.condition))
            .saveSuffixed(output, "_3x")
        // 4x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ELITE)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME / 5)
            .itemOutput(holder, baseCount * 4)
            .apply { subProduct?.let { itemOutput(it, 2) } }
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 5)
            .conditions(listOfNotNull(holder.condition))
            .saveSuffixed(output, "_4x")
        // 5x Chemical
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ULTIMATE)
            .itemInput(HTTagPrefix.ORE, material)
            .fluidInput(RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME / 2)
            .itemOutput(holder, baseCount * 5)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 2)
            .conditions(listOfNotNull(holder.condition))
            .saveSuffixed(output, "_5x")
    }

    private fun registerToDust(output: RecipeOutput, material: HTMaterialKey, dust: SizedHolder) {
        val entry: HTMaterialRegistry.Entry = material.getEntryOrNull() ?: return
        // Raw -> Dust
        if (entry.type.isValidPrefix(HTTagPrefix.RAW_MATERIAL)) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.RAW_MATERIAL, material)
                .itemOutput(dust, 2)
                .conditions(listOfNotNull(dust.condition))
                .saveSuffixed(output, "_from_raw")
        }
        // Gem/Ingot -> Dust
        val mainPrefix: HTTagPrefix = entry.type.getMainPrefix() ?: return
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(mainPrefix, material)
            .itemOutput(dust)
            .conditions(listOfNotNull(dust.condition))
            .saveSuffixed(output, "_from_${mainPrefix.serializedName}")
    }
}
