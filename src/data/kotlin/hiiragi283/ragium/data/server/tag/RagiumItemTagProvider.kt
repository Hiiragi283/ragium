package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTItemTagProvider
import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTBuildingBlockSets
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.tags.MekanismTags
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import top.theillusivec4.curios.api.CuriosTags
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    blockTags: HTTagProvider<Block>,
    helper: ExistingFileHelper,
) : HTItemTagProvider(output, provider, blockTags, helper) {
    override fun addTagsInternal(provider: HolderLookup.Provider) {
        copy()
        material()
        food()
        // armorTags()
        // toolTags()
        category()

        curios()
        pneumatic()
    }

    private fun copy() {
        copyFromBlock(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_OBSIDIAN, RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_QUARTZ, RagiumItemTags.GLASS_BLOCKS_QUARTZ)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_SOUL, RagiumItemTags.GLASS_BLOCKS_SOUL)

        copyFromBlock(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
        copyFromBlock(RagiumBlockTags.OBSIDIANS_MYSTERIOUS, RagiumItemTags.OBSIDIANS_MYSTERIOUS)
    }

    //    Material    //

    private fun material() {
        RagiumBlocks.RAGINITE_ORES.appendItemTags(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendItemTags(this)

        fun register(family: HTMaterialFamily) {
            if (family.isVanilla) return
            for ((variant: HTMaterialFamily.Variant, entry: HTMaterialFamily.Entry) in family.variantMap) {
                val (tagKey: TagKey<Item>, holder: Holder<Item>) = entry
                addTag(variant.commonTag, tagKey)
                add(tagKey, holder)
            }
        }

        register(RagiumMaterialFamilies.RAGI_CRYSTAL)
        register(RagiumMaterialFamilies.CRIMSON_CRYSTAL)
        register(RagiumMaterialFamilies.WARPED_CRYSTAL)

        register(RagiumMaterialFamilies.RAGI_ALLOY)
        register(RagiumMaterialFamilies.ADVANCED_RAGI_ALLOY)
        register(RagiumMaterialFamilies.AZURE_STEEL)
        register(RagiumMaterialFamilies.DEEP_STEEL)

        register(RagiumMaterialFamilies.CHEESE)
        register(RagiumMaterialFamilies.CHOCOLATE)
        // Dusts
        add(RagiumItemTags.DUSTS_ASH, RagiumItems.ASH_DUST)
        add(RagiumItemTags.DUSTS_OBSIDIAN, RagiumItems.OBSIDIAN_DUST)
        add(RagiumItemTags.DUSTS_RAGINITE, RagiumItems.RAGINITE_DUST)
        add(RagiumItemTags.DUSTS_SALTPETER, RagiumItems.SALTPETER_DUST)
        add(RagiumItemTags.DUSTS_SULFUR, RagiumItems.SULFUR_DUST)
        add(RagiumItemTags.DUSTS_WOOD, RagiumItems.SAWDUST)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_ASH)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_OBSIDIAN)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_RAGINITE)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_SALTPETER)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_SULFUR)
        addTag(Tags.Items.DUSTS, RagiumItemTags.DUSTS_WOOD)
        // Raw Materials
        // add(RagiumItemTags.RAW_MATERIALS_RAGINITE, RagiumItems.RAW_RAGINITE)
        // addTag(Tags.Items.RAW_MATERIALS, RagiumItemTags.RAW_MATERIALS_RAGINITE)
        // Storage Blocks
        addItem(RagiumItemTags.STORAGE_BLOCKS_RAGI_CRYSTAL, RagiumBlocks.RAGI_CRYSTAL_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_CRIMSON_CRYSTAL, RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL, RagiumBlocks.WARPED_CRYSTAL_BLOCK)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_RAGI_CRYSTAL)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL)

        addItem(RagiumItemTags.STORAGE_BLOCKS_RAGI_ALLOY, RagiumBlocks.RAGI_ALLOY_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY, RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_AZURE_STEEL, RagiumBlocks.AZURE_STEEL_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_DEEP_STEEL, RagiumBlocks.DEEP_STEEL_BLOCK)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_RAGI_ALLOY)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_AZURE_STEEL)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_DEEP_STEEL)

        addItem(RagiumItemTags.STORAGE_BLOCKS_CHEESE, RagiumBlocks.CHEESE_BLOCK)
        addItem(RagiumItemTags.STORAGE_BLOCKS_CHOCOLATE, RagiumBlocks.CHOCOLATE_BLOCK)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_CHEESE)
        addTag(Tags.Items.STORAGE_BLOCKS, RagiumItemTags.STORAGE_BLOCKS_CHOCOLATE)
        // Mekanism Addon
        add(RagiumItemTags.ENRICHED_AZURE, RagiumMekanismAddon.ITEM_ENRICHED_AZURE)
        add(RagiumItemTags.ENRICHED_RAGINITE, RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE)
        addTag(MekanismTags.Items.ENRICHED, RagiumItemTags.ENRICHED_AZURE)
        addTag(MekanismTags.Items.ENRICHED, RagiumItemTags.ENRICHED_RAGINITE)

        /*fun addMaterialTag(
            prefix: HTTagPrefix,
            material: HTMaterial,
            mod: IntegrationMods,
            path: String,
            type: HTTagBuilder.DependType = HTTagBuilder.DependType.OPTIONAL,
        ) {
            addTag(prefix.itemCommonTag, prefix.createItemTag(material))
            add(prefix.createItemTag(material), mod.createItemHolder<Item>(path), type)
        }*/

        // EIO
        // addMaterialTag(HTTagPrefixes.GEAR, IntegrationMaterials.ENERGETIC_ALLOY, IntegrationMods.EIO, "energized_gear")
        // addMaterialTag(HTTagPrefixes.GEAR, IntegrationMaterials.VIBRANT_ALLOY, IntegrationMods.EIO, "vibrant_gear")
        // Create
        // addMaterialTag(HTTagPrefixes.GEM, IntegrationMaterials.ROSE_QUARTZ, IntegrationMods.CREATE, "rose_quartz")
        // addMaterialTag(HTTagPrefixes.INGOT, IntegrationMaterials.ANDESITE_ALLOY, IntegrationMods.CREATE, "andesite_alloy")
        // Evil Craft
        // addMaterialTag(HTTagPrefixes.DUST, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem_crushed")
        // addMaterialTag(HTTagPrefixes.GEM, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem")
        // addMaterialTag(HTTagPrefixes.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore")
        // addMaterialTag(HTTagPrefixes.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore_deepslate")
        // addMaterialTag(HTTagPrefixes.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_block")
        // MI
        // addMaterialTag(HTTagPrefixes.GEM, CommonMaterials.COAL_COKE, IntegrationMods.MI, "coke")
    }

    //    Food    //

    private fun food() {
        // Crop
        addTag(Tags.Items.CROPS, RagiumItemTags.CROPS_WARPED_WART)
        add(RagiumItemTags.CROPS_WARPED_WART, RagiumItems.WARPED_WART)
        // Food
        add(ItemTags.PIGLIN_LOVED, RagiumItems.FEVER_CHERRY)
        add(Tags.Items.FOODS, RagiumItems.AMBROSIA)
        add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        add(Tags.Items.FOODS, RagiumItems.COOKED_MEAT_INGOT)
        add(Tags.Items.FOODS, RagiumItems.EXP_BERRIES)
        add(Tags.Items.FOODS, RagiumItems.FEVER_CHERRY)
        add(Tags.Items.FOODS, RagiumItems.ICE_CREAM)
        add(Tags.Items.FOODS, RagiumItems.ICE_CREAM_SODA)
        add(Tags.Items.FOODS, RagiumItems.MEAT_INGOT)
        add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_PIECE)
        add(Tags.Items.FOODS, RagiumItems.WARPED_WART)
        add(Tags.Items.FOODS_BERRY, RagiumItems.EXP_BERRIES)
        add(Tags.Items.FOODS_FRUIT, RagiumItems.FEVER_CHERRY)
        add(Tags.Items.FOODS_GOLDEN, RagiumItems.FEVER_CHERRY)
        addTag(Tags.Items.FOODS, RagiumItemTags.FOODS_CHEESE)
        addTag(Tags.Items.FOODS, RagiumItemTags.FOODS_CHOCOLATE)
        addTag(Tags.Items.FOODS, RagiumItemTags.FOODS_JAMS)
        addTag(Tags.Items.FOODS_FRUIT, RagiumItemTags.FOODS_CHERRY)

        add(RagiumItemTags.FOODS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP)
        add(RagiumItemTags.FOODS_RAGI_CHERRY, RagiumItems.RAGI_CHERRY)
        add(RagiumItemTags.JAMS_RAGI_CHERRY, RagiumItems.RAGI_CHERRY_JAM)
        addTag(RagiumItemTags.FOODS_CHERRY, RagiumItemTags.FOODS_RAGI_CHERRY)
        addTag(RagiumItemTags.FOODS_JAMS, RagiumItemTags.JAMS_RAGI_CHERRY)

        addTag(RagiumItemTags.FOODS_CHEESE, RagiumItemTags.INGOTS_CHEESE)
        addTag(RagiumItemTags.FOODS_CHOCOLATE, RagiumItemTags.INGOTS_CHOCOLATE)
    }

    //    Armor    //

    /*private fun armorTags() {
        add(ItemTags.HEAD_ARMOR_ENCHANTABLE, RagiumItems.DIVING_GOGGLE)
        add(ItemTags.CHEST_ARMOR_ENCHANTABLE, RagiumItems.JETPACK)
        add(ItemTags.LEG_ARMOR_ENCHANTABLE, RagiumItems.CLIMBING_LEGGINGS)
        add(ItemTags.FOOT_ARMOR_ENCHANTABLE, RagiumItems.SLIME_BOOTS)

        RagiumItems.AZURE_STEEL_ARMORS.appendItemTags(
        RagiumItems.DURALUMIN_ARMORS.appendItemTags(
    }

    //    Tool    //

    private fun toolTags() {
        add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.RAGI_LANTERN)
        add(ItemTags.PICKAXES, RagiumItems.FEVER_PICKAXE)
        add(ItemTags.PICKAXES, RagiumItems.SILKY_PICKAXE)
        addTag(ForgeTool.TAG, RagiumItemTags.TOOLS_FORGE_HAMMER)
        addTag(ItemTags.DURABILITY_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)
        addTag(ItemTags.MINING_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)
        addTag(ItemTags.MINING_LOOT_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)

        RagiumItems.RAGI_ALLOY_TOOLS.appendItemTags(
        RagiumItems.AZURE_STEEL_TOOLS.appendItemTags(
        RagiumItems.DURALUMIN_TOOLS.appendItemTags(

        for (dynamite: DeferredItem<out HTThrowableItem> in RagiumItems.DYNAMITES) {
            add(RagiumItemTags.DYNAMITES, dynamite)
        }
    }*/

    //    Category    //

    @Suppress("DEPRECATION")
    private fun category() {
        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.appendItemTags(this)
        }

        for (block: DeferredBlock<*> in RagiumBlocks.LED_BLOCKS.values) {
            addItem(RagiumItemTags.LED_BLOCKS, block)
        }

        RagiumItems.AZURE_STEEL_ARMORS.appendItemTags(this)
        // Tools
        add(RagiumItemTags.TOOLS_FORGE_HAMMER, RagiumItems.RAGI_ALLOY_HAMMER)
        addTag(Tags.Items.TOOLS, RagiumItemTags.TOOLS_FORGE_HAMMER)

        RagiumItems.AZURE_STEEL_TOOLS.appendItemTags(this)

        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            add(content.bucketTag, content.bucketHolder)
            addTag(Tags.Items.BUCKETS, content.bucketTag)
        }

        // Parts
        add(itemTagKey(commonId("plates/plastic")), RagiumItems.PLASTIC_PLATE)
        add(RagiumItemTags.PLASTICS, RagiumItems.PLASTIC_PLATE)
        add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        add(Tags.Items.SLIMEBALLS, RagiumItems.TAR)
        addItem(RagiumItemTags.PAPER, Items.PAPER)

        // Circuit
        add(RagiumItemTags.CIRCUITS_BASIC, RagiumItems.BASIC_CIRCUIT)
        add(RagiumItemTags.CIRCUITS_ADVANCED, RagiumItems.ADVANCED_CIRCUIT)
        add(RagiumItemTags.CIRCUITS_ELITE, RagiumItems.CRYSTAL_PROCESSOR)
        addTag(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_BASIC)
        addTag(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_ADVANCED)
        addTag(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_ELITE)

        // Soil
        addItem(RagiumItemTags.DIRT_SOILS, Items.DIRT)
        addItem(RagiumItemTags.DIRT_SOILS, Items.FARMLAND)
        addItem(RagiumItemTags.DIRT_SOILS, Items.GRASS_BLOCK)
        addItem(RagiumItemTags.MUSHROOM_SOILS, Items.MYCELIUM)
        addItem(RagiumItemTags.NETHER_SOILS, Items.CRIMSON_NYLIUM)
        addItem(RagiumItemTags.NETHER_SOILS, Items.WARPED_NYLIUM)
        addTag(RagiumItemTags.END_SOILS, Tags.Items.END_STONES)

        // Mold
        for (mold: RagiumItems.Molds in RagiumItems.Molds.entries) {
            addTag(RagiumItemTags.MOLDS, mold.tagKey)
            addItem(mold.tagKey, mold)
        }
    }

    //    Integrations    //

    private fun curios() {
        add(CuriosTags.CHARM, RagiumItems.EXP_MAGNET)
        add(CuriosTags.CHARM, RagiumItems.ITEM_MAGNET)
        add(CuriosTags.CHARM, RagiumItems.RAGI_LANTERN)
    }

    private fun pneumatic() {
        add(PneumaticCraftTags.Items.PLASTIC_SHEETS, RagiumItems.PLASTIC_PLATE)
    }
}
