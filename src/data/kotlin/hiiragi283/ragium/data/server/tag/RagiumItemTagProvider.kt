package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTItemTagProvider
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTMaterial
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
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
    }

    private fun copy() {
        copyFromBlock(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS)

        copyFromBlock(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_OBSIDIAN, RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_QUARTZ, RagiumItemTags.GLASS_BLOCKS_QUARTZ)
        copyFromBlock(RagiumBlockTags.GLASS_BLOCKS_SOUL, RagiumItemTags.GLASS_BLOCKS_SOUL)

        copyFromBlock(RagiumBlockTags.OBSIDIANS_MYSTERIOUS, RagiumItemTags.OBSIDIANS_MYSTERIOUS)
    }

    //    Material    //

    private fun material() {
        RagiumBlocks.RAGINITE_ORES.appendItemTags(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendItemTags(this)

        fun addMaterialTag(
            prefix: HTTagPrefix,
            material: HTMaterial,
            item: ItemLike,
            type: HTTagBuilder.DependType = HTTagBuilder.DependType.REQUIRED,
        ) {
            addTag(prefix.itemCommonTag, prefix.createItemTag(material))
            addItem(prefix, material, item, type)
        }

        fun register(entries: List<HTMaterialItemLike>) {
            for (item: HTMaterialItemLike in entries) {
                addMaterialTag(item.prefix, item, item)
            }
        }

        register(RagiumBlocks.StorageBlocks.entries)
        register(RagiumItems.Dusts.entries)
        register(RagiumItems.Ingots.entries)
        register(RagiumItems.RawResources.entries)
        register(RagiumMekanismAddon.OreResources.entries)

        addMaterialTag(HTTagPrefixes.DUST, VanillaMaterials.WOOD, RagiumItems.SAWDUST)

        addTag(
            HTTagPrefixes.GEM.createItemTag(CommonMaterials.COAL_COKE),
            RagiumItemTags.COAL_COKE,
            HTTagBuilder.DependType.OPTIONAL,
        )
        addTag(
            HTTagPrefixes.GEM.createItemTag(VanillaMaterials.ENDER_PEARL),
            Tags.Items.ENDER_PEARLS,
        )

        addItem(HTTagPrefixes.STORAGE_BLOCK, VanillaMaterials.AMETHYST, Items.AMETHYST_BLOCK)
        addItem(HTTagPrefixes.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE, Items.GLOWSTONE)
        addItem(HTTagPrefixes.GEM, VanillaMaterials.COAL, Items.COAL)
        addItem(HTTagPrefixes.GEM, VanillaMaterials.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)

        fun addMaterialTag(
            prefix: HTTagPrefix,
            material: HTMaterial,
            mod: IntegrationMods,
            path: String,
            type: HTTagBuilder.DependType = HTTagBuilder.DependType.OPTIONAL,
        ) {
            addTag(prefix.itemCommonTag, prefix.createItemTag(material))
            add(prefix.createItemTag(material), mod.createItemHolder<Item>(path), type)
        }

        // EIO
        addMaterialTag(HTTagPrefixes.GEAR, IntegrationMaterials.ENERGETIC_ALLOY, IntegrationMods.EIO, "energized_gear")
        addMaterialTag(HTTagPrefixes.GEAR, IntegrationMaterials.VIBRANT_ALLOY, IntegrationMods.EIO, "vibrant_gear")
        // Create
        addMaterialTag(HTTagPrefixes.GEM, IntegrationMaterials.ROSE_QUARTZ, IntegrationMods.CREATE, "rose_quartz")
        addMaterialTag(HTTagPrefixes.INGOT, IntegrationMaterials.ANDESITE_ALLOY, IntegrationMods.CREATE, "andesite_alloy")
        // Evil Craft
        addMaterialTag(HTTagPrefixes.DUST, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem_crushed")
        addMaterialTag(HTTagPrefixes.GEM, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem")
        addMaterialTag(HTTagPrefixes.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore")
        addMaterialTag(HTTagPrefixes.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore_deepslate")
        addMaterialTag(HTTagPrefixes.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_block")
        // MI
        addMaterialTag(HTTagPrefixes.GEM, CommonMaterials.COAL_COKE, IntegrationMods.MI, "coke")
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
        addTag(RagiumItemTags.FOODS_CHEESE, HTTagPrefixes.INGOT.createItemTag(CommonMaterials.CHEESE))
        addTag(RagiumItemTags.FOODS_CHERRY, RagiumItemTags.FOODS_RAGI_CHERRY)
        addTag(RagiumItemTags.FOODS_CHOCOLATE, HTTagPrefixes.INGOT.createItemTag(CommonMaterials.CHOCOLATE))
        addTag(RagiumItemTags.FOODS_JAMS, RagiumItemTags.JAMS_RAGI_CHERRY)
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
        RagiumBlocks.RAGI_STONE_SETS.appendItemTags(this)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.appendItemTags(this)
        RagiumBlocks.AZURE_TILE_SETS.appendItemTags(this)
        RagiumBlocks.EMBER_STONE_SETS.appendItemTags(this)
        RagiumBlocks.PLASTIC_SETS.appendItemTags(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.appendItemTags(this)

        for (block: DeferredBlock<*> in RagiumBlocks.LED_BLOCKS.values) {
            addItem(RagiumItemTags.LED_BLOCKS, block)
        }

        RagiumItems.AZURE_STEEL_ARMORS.appendItemTags(this)

        RagiumItems.RAGI_ALLOY_TOOLS.appendItemTags(this)
        RagiumItems.AZURE_STEEL_TOOLS.appendItemTags(this)

        // Bucket
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
        addTag(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_BASIC)
        addTag(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_ADVANCED)

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

    //    Curios Addon    //

    private fun curios() {
        add(CuriosTags.CHARM, RagiumItems.EXP_MAGNET)
        add(CuriosTags.CHARM, RagiumItems.ITEM_MAGNET)
        add(CuriosTags.CHARM, RagiumItems.RAGI_LANTERN)
    }
}
