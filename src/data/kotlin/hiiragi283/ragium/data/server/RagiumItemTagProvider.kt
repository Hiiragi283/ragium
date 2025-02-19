package hiiragi283.ragium.data.server

import aztech.modern_industrialization.items.ForgeTool
import aztech.modern_industrialization.materials.MIMaterials
import aztech.modern_industrialization.materials.part.MIParts
import blusunrize.immersiveengineering.api.wires.WireType
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import mekanism.generators.common.registries.GeneratorsItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import vectorwing.farmersdelight.common.registry.ModBlocks
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Item>(output, Registries.ITEM, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    lateinit var builder: HTTagBuilder<Item>

    override fun addTags(provider: HolderLookup.Provider) {
        builder = HTTagBuilder(provider.lookupOrThrow(Registries.ITEM))

        materialTags()
        foodTags()
        armorTags()
        toolTags()
        partTags()
        enchantmentTags()

        builder.build { tagKey: TagKey<Item>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }

        tag(ItemTags.COALS).remove(IEItems.Ingredients.COAL_COKE.regObject.id)
    }

    //    Material    //

    private fun materialTags() {
        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            val oreTagKey: TagKey<Item> = HTTagPrefix.ORE.createTag(key)
            builder.addTag(HTTagPrefix.ORE.commonTagKey, oreTagKey)
            builder.add(oreTagKey, ore.asHolder())
        }

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            val storageTag: TagKey<Item> = HTTagPrefix.STORAGE_BLOCK.createTag(key)
            builder.addTag(HTTagPrefix.STORAGE_BLOCK.commonTagKey, storageTag)
            builder.add(storageTag, storage.asHolder())
        }

        RagiumItems.MATERIAL_ITEMS.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            val tagKey: TagKey<Item> = prefix.createTag(key)
            builder.addTag(prefix.commonTagKey, tagKey)
            builder.add(tagKey, holder)
            if (prefix == HTTagPrefix.INGOT) {
                builder.addTag(ItemTags.BEACON_PAYMENT_ITEMS, tagKey)
            }
        }

        builder.addTag(HTTagPrefix.GEM.createTag(CommonMaterials.COAL_COKE), RagiumItemTags.COAL_COKE, true)

        addMaterialTag(HTTagPrefix.COIL, CommonMaterials.ELECTRUM, IEItems.Misc.WIRE_COILS[WireType.ELECTRUM])
        addMaterialTag(HTTagPrefix.COIL, CommonMaterials.STEEL, IEItems.Misc.WIRE_COILS[WireType.STEEL])
        addMaterialTag(HTTagPrefix.COIL, VanillaMaterials.COPPER, IEItems.Misc.WIRE_COILS[WireType.COPPER])
        addMaterialTag(HTTagPrefix.GEM, CommonMaterials.COAL_COKE, MIMaterials.COKE.getPart(MIParts.GEM), false)
        addMaterialTag(HTTagPrefix.GEM, VanillaMaterials.NETHERITE_SCRAP, Items.NETHERITE_SCRAP, false)

        addMaterialTag(HTTagPrefix.DUST, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem_crushed")
        addMaterialTag(HTTagPrefix.GEM, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore_deepslate")
        addMaterialTag(HTTagPrefix.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, "evilcraft:dark_block")
    }

    private fun addMaterialTag(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        item: ItemLike?,
        optional: Boolean = true,
    ) {
        builder.addTag(prefix.commonTagKey, prefix.createTag(material))
        item?.let { builder.add(prefix.createTag(material), it.asHolder(), optional) }
    }

    private fun addMaterialTag(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        value: String,
        optional: Boolean = true,
    ) {
        builder.addTag(prefix.commonTagKey, prefix.createTag(material))
        builder.add(prefix.createTag(material), DeferredItem.createItem<Item>(ResourceLocation.parse(value)), optional)
    }

    //    Food    //

    private fun foodTags() {
        RagiumItems.FOODS.forEach { foodItem: DeferredItem<out Item> ->
            if (foodItem.get().components().has(DataComponents.FOOD)) {
                builder.add(Tags.Items.FOODS, foodItem)
            }
        }

        builder.add(RagiumItemTags.DOUGH, RagiumItems.DOUGH)
    }

    //    Armor    //

    private fun armorTags() {
        builder.add(ItemTags.HEAD_ARMOR_ENCHANTABLE, RagiumItems.DIVING_GOGGLE)
        builder.add(ItemTags.CHEST_ARMOR_ENCHANTABLE, RagiumItems.JETPACK)

        RagiumItems.STEEL_ARMORS.appendTags(builder::add)
    }

    //    Tool    //

    private fun toolTags() {
        builder.add(ForgeTool.TAG, RagiumItems.FORGE_HAMMER)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.FORGE_HAMMER)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.RAGI_LANTERN)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.STEEL_SHEARS)
        builder.add(ItemTags.MINING_ENCHANTABLE, RagiumItems.STEEL_SHEARS)
        builder.add(ItemTags.PICKAXES, RagiumItems.FEVER_PICKAXE)
        builder.add(ItemTags.PICKAXES, RagiumItems.SILKY_PICKAXE)

        RagiumItems.STEEL_TOOLS.appendTags(builder::add)
    }

    //    Part    //

    private fun partTags() {
        builder.add(RagiumItemTags.PLASTICS, RagiumItems.PLASTIC_PLATE)
        builder.add(itemTagKey(commonId("plates/plastic")), RagiumItems.PLASTIC_PLATE)

        builder.add(RagiumItemTags.BASIC_CIRCUIT, RagiumItems.BASIC_CIRCUIT)
        builder.add(RagiumItemTags.ADVANCED_CIRCUIT, RagiumItems.ADVANCED_CIRCUIT)
        builder.add(RagiumItemTags.ELITE_CIRCUIT, RagiumItems.ELITE_CIRCUIT)
        builder.add(RagiumItemTags.ULTIMATE_CIRCUIT, RagiumItems.ULTIMATE_CIRCUIT)

        builder.add(RagiumItemTags.SLAG, RagiumItems.SLAG)
        builder.add(RagiumItemTags.SOLAR_PANELS, GeneratorsItems.SOLAR_PANEL, true)
        builder.add(RagiumItemTags.SOLAR_PANELS, RagiumItems.SOLAR_PANEL)
        builder.add(Tags.Items.BUCKETS, RagiumItems.CRUDE_OIL_BUCKET)

        builder.add(RagiumItemTags.DIRT_SOILS, Items.FARMLAND.asHolder())
        builder.add(RagiumItemTags.DIRT_SOILS, ModBlocks.RICH_SOIL.get().asHolder(), true)
        builder.add(RagiumItemTags.DIRT_SOILS, ModBlocks.RICH_SOIL_FARMLAND.get().asHolder(), true)
        builder.addTag(RagiumItemTags.DIRT_SOILS, ItemTags.DIRT)

        builder.add(RagiumItemTags.MUSHROOM_SOILS, Items.MYCELIUM.asHolder())

        builder.add(RagiumItemTags.NETHER_SOILS, Items.CRIMSON_NYLIUM.asHolder())
        builder.add(RagiumItemTags.NETHER_SOILS, Items.WARPED_NYLIUM.asHolder())

        builder.addTag(RagiumItemTags.END_SOILS, Tags.Items.END_STONES)

        builder.add(RagiumItemTags.BALL_MOLDS, RagiumItems.BALL_PRESS_MOLD)
        builder.add(RagiumItemTags.GEAR_MOLDS, IEItems.Molds.MOLD_GEAR.asHolder(), true)
        builder.add(RagiumItemTags.GEAR_MOLDS, RagiumItems.GEAR_PRESS_MOLD)
        builder.add(RagiumItemTags.PLATE_MOLDS, IEItems.Molds.MOLD_PLATE.asHolder(), true)
        builder.add(RagiumItemTags.PLATE_MOLDS, RagiumItems.PLATE_PRESS_MOLD)
        builder.add(RagiumItemTags.ROD_MOLDS, IEItems.Molds.MOLD_ROD.asHolder(), true)
        builder.add(RagiumItemTags.ROD_MOLDS, RagiumItems.ROD_PRESS_MOLD)
        builder.add(RagiumItemTags.WIRE_MOLDS, IEItems.Molds.MOLD_WIRE.asHolder(), true)
        builder.add(RagiumItemTags.WIRE_MOLDS, RagiumItems.WIRE_PRESS_MOLD)

        RagiumBlocks.LED_BLOCKS.values.forEach { builder.add(RagiumItemTags.LED_BLOCKS, it.asHolder()) }
    }

    //    Enchantment    //

    private fun enchantmentTags() {
        buildList {
            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            add(RagiumBlocks.COPPER_DRUM)

            addAll(HTMachineType.getBlocks())
        }.map(ItemLike::asHolder)
            .forEach { holder: Holder.Reference<Item> ->
                builder.add(ItemTags.DURABILITY_ENCHANTABLE, holder)
                builder.add(ItemTags.MINING_ENCHANTABLE, holder)
                builder.add(ItemTags.MINING_LOOT_ENCHANTABLE, holder)
                builder.add(RagiumItemTags.CAPACITY_ENCHANTABLE, holder)
            }
    }
}
