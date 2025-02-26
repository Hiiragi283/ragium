package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTGrowthChamberRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.event.HTMachineRecipesUpdatedEvent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipes {
    @SubscribeEvent
    fun onMachineRecipesUpdated(event: HTMachineRecipesUpdatedEvent) {
        blastFurnace(event)
        compressor(event)
        extractor(event)
        grinder(event)
        infuser(event)
    }

    @JvmStatic
    private fun blastFurnace(event: HTMachineRecipesUpdatedEvent) {
        event.register(
            HTRecipeTypes.BLAST_FURNACE,
            RagiumAPI.id("brass_ingot"),
        ) { lookup: HolderGetter<Item> ->
            event.getFirstHolder(HTTagPrefix.INGOT, CommonMaterials.BRASS)
            val ingot: Item = event
                .getFirstItem(HTTagPrefix.INGOT, CommonMaterials.BRASS)
                ?: return@register null
            HTMultiItemRecipeBuilder
                .blastFurnace(lookup)
                .itemInput(HTTagPrefix.DUST, VanillaMaterials.COPPER, 3)
                .itemInput(HTTagPrefix.DUST, CommonMaterials.ZINC)
                .itemOutput(ingot, 4)
        }
    }

    @JvmStatic
    private fun compressor(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            if (mainPrefix != null) {
                // Gear
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_gear"),
                ) { lookup: HolderGetter<Item> ->
                    val gear: Item = event.getFirstItem(HTTagPrefix.GEAR, key) ?: return@register null
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key, 4)
                        .catalyst(RagiumItemTags.GEAR_MOLDS)
                        .itemOutput(gear)
                }
                // Plate
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_plate"),
                ) { lookup: HolderGetter<Item> ->
                    val plate: Item = event.getFirstItem(HTTagPrefix.PLATE, key) ?: return@register null
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.PLATE_MOLDS)
                        .itemOutput(plate)
                }
                // Rod
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_rod"),
                ) { lookup: HolderGetter<Item> ->
                    val rod: Item = event.getFirstItem(HTTagPrefix.ROD, key) ?: return@register null
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.ROD_MOLDS)
                        .itemOutput(rod, 2)
                }
                // Wire
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_wire"),
                ) { lookup: HolderGetter<Item> ->
                    val wire: Item = event.getFirstItem(HTTagPrefix.WIRE, key) ?: return@register null
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.WIRE_MOLDS)
                        .itemOutput(wire, 2)
                }
            }
            // Gem
            event.register(
                HTRecipeTypes.COMPRESSOR,
                RagiumAPI.id("runtime_${name}_gem"),
            ) { lookup: HolderGetter<Item> ->
                val gem: Item = event.getFirstItem(HTTagPrefix.GEM, key) ?: return@register null
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(HTTagPrefix.DUST, key)
                    .itemOutput(gem)
            }
        }
    }

    @JvmStatic
    private fun extractor(event: HTMachineRecipesUpdatedEvent) {
        // Fluid Bucket -> Bucket + Fluid
        event
            .fluidLookup()
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                event.register(
                    HTRecipeTypes.EXTRACTOR,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .extractor(lookup)
                        .itemInput(bucket)
                        .itemOutput(Items.BUCKET)
                        .fluidOutput(fluid)
                }
            }
    }

    @JvmStatic
    private fun grinder(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                event.register(
                    HTRecipeTypes.GRINDER,
                    RagiumAPI.id("runtime_${name}_dust_from_ore"),
                ) { lookup: HolderGetter<Item> ->
                    val result: Item = event.getFirstItem(resultPrefix, key) ?: return@register null
                    val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
                    HTSingleItemRecipeBuilder
                        .grinder(lookup)
                        .itemInput(HTTagPrefix.ORE, key)
                        .itemOutput(result, count * 2)
                }
            }
            val dust: Item = event.getFirstItem(HTTagPrefix.DUST, key) ?: continue
            // Gem/Ingot
            if (mainPrefix != null) {
                event.register(
                    HTRecipeTypes.GRINDER,
                    RagiumAPI.id("runtime_${name}_dust_from_main"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .grinder(lookup)
                        .itemInput(mainPrefix, key)
                        .itemOutput(dust)
                }
            }
            // Gear
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_gear"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.GEAR, key)
                    .itemOutput(dust, 4)
            }
            // Plate
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_plate"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.PLATE, key)
                    .itemOutput(dust)
            }
            // Raw
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_raw"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.RAW_MATERIAL, key, 4)
                    .itemOutput(dust, 3)
            }
        }
    }

    @JvmStatic
    private fun growth(event: HTMachineRecipesUpdatedEvent) {
        event
            .itemLookup()
            .listTags()
            .forEach { holderSet: HolderSet.Named<Item> ->
                val tagKey: TagKey<Item> = holderSet.key()
                val tagId: ResourceLocation = tagKey.location
                if (tagId.namespace != "c") return@forEach
                if (!tagId.path.startsWith("crops/")) return@forEach
                val cropName: String = tagId.path.removePrefix("crops/")
                val firstCrop: Holder<Item> = holderSet.firstOrNull() ?: return@forEach
                event.register(
                    HTRecipeTypes.GROWTH_CHAMBER,
                    RagiumAPI.id("runtime_crops_$cropName"),
                ) { lookup: HolderGetter<Item> ->
                    HTGrowthChamberRecipeBuilder(lookup)
                        .itemInput(itemTagKey(commonId("seeds/$cropName")))
                        .itemInput(RagiumItemTags.DIRT_SOILS)
                        .itemOutput(firstCrop.value(), 2)
                }
            }
    }

    @JvmStatic
    private fun infuser(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val resultPrefix: HTTagPrefix = type.getOreResultPrefix() ?: continue
            val result: Item = event.getFirstItem(resultPrefix, key) ?: continue
            val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
            // 3x
            event.register(
                HTRecipeTypes.INFUSER,
                RagiumAPI.id("runtime_3x_$name"),
            ) { lookup: HolderGetter<Item> ->
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
                    .itemOutput(result, count * 3)
            }
            // 4x
            event.register(
                HTRecipeTypes.INFUSER,
                RagiumAPI.id("runtime_4x_$name"),
            ) { lookup: HolderGetter<Item> ->
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 500)
                    .itemOutput(result, count * 4)
            }
        }
        // Bucket + Fluid -> Fluid Bucket
        event
            .fluidLookup()
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                event.register(
                    HTRecipeTypes.INFUSER,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}_bucket"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .infuser(lookup)
                        .itemInput(Items.BUCKET)
                        .fluidInput(fluid)
                        .itemOutput(bucket)
                }
            }
        // Oxidizables
        event
            .blockLookup()
            .listElements()
            .forEach { holder: Holder.Reference<Block> ->
                val block: Block = holder.value()
                if (ItemStack(block).isEmpty) return@forEach
                val block1: Block = holder.getData(NeoForgeDataMaps.OXIDIZABLES)?.nextOxidationStage ?: return@forEach
                if (ItemStack(block1).isEmpty) return@forEach
                // Oxidization
                event.register(
                    HTRecipeTypes.INFUSER,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}_oxidization"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .infuser(lookup)
                        .itemInput(block)
                        .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 100)
                        .itemOutput(block1)
                }
                // Reduction
                event.register(
                    HTRecipeTypes.INFUSER,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}_reduction"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .infuser(lookup)
                        .itemInput(block1)
                        .fluidInput(RagiumVirtualFluids.HYDROGEN.commonTag, 200)
                        .itemOutput(block)
                        .waterOutput(200)
                }
            }
    }
}
