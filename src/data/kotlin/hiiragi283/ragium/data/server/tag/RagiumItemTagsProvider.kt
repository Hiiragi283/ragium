package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.HTForgeHammerItem
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTArmorSets
import hiiragi283.ragium.util.HTToolSets
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.tags.MekanismTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem
import top.theillusivec4.curios.api.CuriosTags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
    helper: ExistingFileHelper,
) : ItemTagsProvider(
        output,
        provider,
        blockTags,
        RagiumAPI.MOD_ID,
        helper,
    ) {
    private fun IntrinsicTagAppender<Item>.addItem(item: ItemLike): IntrinsicTagAppender<Item> = apply {
        add(item.asItem())
    }

    private fun addItem(parent: TagKey<Item>, child: TagKey<Item>, item: ItemLike) {
        tag(parent).addTag(child)
        tag(child).addItem(item)
    }

    override fun addTags(provider: HolderLookup.Provider) {
        copy()

        materials()
        foods()
        categories()

        curios()
        pneumatic()
    }

    private fun copy() {
        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)

        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(RagiumCommonTags.Blocks.ORES_RAGINITE, RagiumCommonTags.Items.ORES_RAGINITE)
        copy(RagiumCommonTags.Blocks.ORES_RAGI_CRYSTAL, RagiumCommonTags.Items.ORES_RAGI_CRYSTAL)
        copy(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        copy(RagiumCommonTags.Blocks.GLASS_BLOCKS_OBSIDIAN, RagiumCommonTags.Items.GLASS_BLOCKS_OBSIDIAN)
        copy(RagiumCommonTags.Blocks.GLASS_BLOCKS_QUARTZ, RagiumCommonTags.Items.GLASS_BLOCKS_QUARTZ)
        copy(RagiumCommonTags.Blocks.GLASS_BLOCKS_SOUL, RagiumCommonTags.Items.GLASS_BLOCKS_SOUL)
        copy(RagiumModTags.Blocks.LED_BLOCKS, RagiumModTags.Items.LED_BLOCKS)
        copy(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)

        copy(RagiumModTags.Blocks.WIP, RagiumModTags.Items.WIP)
    }

    //    Material    //

    private fun materials() {
        for (family: HTMaterialFamily in HTMaterialFamily.instances.values) {
            if (family.entryType == HTMaterialFamily.EntryType.RAGIUM) {
                for ((variant: HTMaterialFamily.Variant, tagKey: TagKey<Item>, item: ItemLike?) in family) {
                    if (item != null) {
                        tag(variant.commonTag).addTag(tagKey)
                        tag(tagKey).add(item.asItem())
                    }
                }
            }
        }

        // Dusts
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_ASH, RagiumItems.ASH_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_CINNABAR, RagiumItems.CINNABAR_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_OBSIDIAN, RagiumItems.OBSIDIAN_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_QUARTZ, RagiumItems.QUARTZ_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_RAGINITE, RagiumItems.RAGINITE_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_SALTPETER, RagiumItems.SALTPETER_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_SULFUR, RagiumItems.SULFUR_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_WOOD, RagiumItems.SAWDUST)
        // Plates
        addItem(RagiumCommonTags.Items.PLATES, RagiumCommonTags.Items.PLATES_PLASTIC, RagiumItems.PLASTIC_PLATE)

        // Mekanism Addon
        tag(RagiumCommonTags.Items.ENRICHED_AZURE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE)
        tag(RagiumCommonTags.Items.ENRICHED_RAGINITE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE)
        tag(MekanismTags.Items.ENRICHED)
            .addTag(RagiumCommonTags.Items.ENRICHED_AZURE)
            .addTag(RagiumCommonTags.Items.ENRICHED_RAGINITE)
    }

    private fun foods() {
        addItem(Tags.Items.CROPS, RagiumCommonTags.Items.CROPS_WARPED_WART, RagiumItems.WARPED_WART)

        tag(Tags.Items.FOODS)
            .addTag(RagiumCommonTags.Items.FOODS_CHOCOLATE)
            .addTag(RagiumCommonTags.Items.FOODS_JAMS)
            .addTag(RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
            .addTag(RagiumCommonTags.Items.INGOTS_MEAT)
            .addItem(RagiumItems.AMBROSIA)
            .addItem(RagiumItems.CANNED_COOKED_MEAT)
            .addItem(RagiumItems.FEVER_CHERRY)
            .addItem(RagiumItems.ICE_CREAM)
            .addItem(RagiumItems.ICE_CREAM_SODA)
            .addItem(RagiumItems.MEAT_INGOT)
            .addItem(RagiumItems.MELON_PIE)
            .addItem(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addItem(RagiumItems.WARPED_WART)

        tag(Tags.Items.FOODS_BERRY).addItem(RagiumItems.EXP_BERRIES)
        tag(Tags.Items.FOODS_GOLDEN).addItem(RagiumItems.FEVER_CHERRY)

        tag(Tags.Items.FOODS_FRUIT)
            .addTag(RagiumCommonTags.Items.FOODS_CHERRY)
            .addItem(RagiumItems.FEVER_CHERRY)
        tag(RagiumCommonTags.Items.FOODS_CHERRY).addTag(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
        tag(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addItem(RagiumItems.RAGI_CHERRY)
            .addItem(RagiumDelightAddon.RAGI_CHERRY_PULP)

        tag(RagiumCommonTags.Items.FOODS_JAMS).addTag(RagiumCommonTags.Items.JAMS_RAGI_CHERRY)
        tag(RagiumCommonTags.Items.JAMS_RAGI_CHERRY).addItem(RagiumItems.RAGI_CHERRY_JAM)

        tag(RagiumCommonTags.Items.FOODS_CHOCOLATE).addTag(RagiumCommonTags.Items.INGOTS_CHOCOLATE)
    }

    private fun categories() {
        tag(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .addTag(ItemTags.SMELTS_TO_GLASS)

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .addTag(RagiumCommonTags.Items.DUSTS_CINNABAR)
            .addTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)

        // Armors
        fun registerArmors(armorSets: HTArmorSets) {
            tag(ItemTags.HEAD_ARMOR).addItem(armorSets.helmetItem)
            tag(ItemTags.CHEST_ARMOR).addItem(armorSets.chestplateItem)
            tag(ItemTags.LEG_ARMOR).addItem(armorSets.leggingsItem)
            tag(ItemTags.FOOT_ARMOR).addItem(armorSets.bootsItem)
        }
        registerArmors(RagiumItems.AZURE_STEEL_ARMORS)
        registerArmors(RagiumItems.DEEP_STEEL_ARMORS)

        // Tools
        fun registerTools(toolSets: HTToolSets) {
            tag(ItemTags.AXES).addItem(toolSets.axeItem)
            tag(ItemTags.HOES).addItem(toolSets.hoeItem)
            tag(ItemTags.PICKAXES).addItem(toolSets.pickaxeItem)
            tag(ItemTags.SHOVELS).addItem(toolSets.shovelItem)
            tag(ItemTags.SWORDS).addItem(toolSets.swordItem)
        }
        registerTools(RagiumItems.AZURE_STEEL_TOOLS)
        registerTools(RagiumItems.DEEP_STEEL_TOOLS)

        for (hammer: DeferredItem<HTForgeHammerItem> in RagiumItems.FORGE_HAMMERS.values) {
            tag(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER).addItem(hammer)
            tag(Tags.Items.TOOLS_WRENCH).addItem(hammer)
        }

        listOf(
            ItemTags.DURABILITY_ENCHANTABLE,
            ItemTags.VANISHING_ENCHANTABLE,
            Tags.Items.TOOLS,
        ).map(::tag).forEach { it.addTag(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER) }

        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            addItem(Tags.Items.BUCKETS, content.bucketTag, content.bucketHolder)
        }

        // Parts
        tag(Tags.Items.SLIME_BALLS).addItem(RagiumItems.TAR)
        tag(RagiumCommonTags.Items.PAPER).addItem(Items.PAPER)
        tag(RagiumModTags.Items.POLYMER_RESIN)
            .addItem(RagiumItems.POLYMER_RESIN)
            .addOptional(ResourceLocation.fromNamespaceAndPath(RagiumConstantValues.ORITECH, "polymer_resin"))

        tag(RagiumCommonTags.Items.PLASTICS).addOptionalTag(RagiumCommonTags.Items.PLATES_PLASTIC)
        tag(RagiumCommonTags.Items.PLATES_PLASTIC).addOptionalTag(RagiumCommonTags.Items.PLASTICS)

        tag(Tags.Items.LEATHERS).addItem(RagiumItems.SYNTHETIC_LEATHER)
        tag(Tags.Items.STRINGS).addItem(RagiumItems.SYNTHETIC_FIBER)
        // Circuits
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_BASIC, RagiumItems.BASIC_CIRCUIT)
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_ADVANCED, RagiumItems.ADVANCED_CIRCUIT)
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_ELITE, RagiumItems.ELITE_CIRCUIT)
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_ULTIMATE, RagiumItems.ULTIMATE_CIRCUIT)

        tag(RagiumModTags.Items.ENI_UPGRADES)
            .addTag(RagiumModTags.Items.ENI_UPGRADES_BASIC)
            .addTag(RagiumModTags.Items.ENI_UPGRADES_ADVANCED)
            .addTag(RagiumModTags.Items.ENI_UPGRADES_ELITE)
        tag(RagiumModTags.Items.ENI_UPGRADES_BASIC).addTag(RagiumCommonTags.Items.CIRCUITS_BASIC)
        tag(RagiumModTags.Items.ENI_UPGRADES_ADVANCED).addTag(RagiumCommonTags.Items.CIRCUITS_ADVANCED)
        tag(RagiumModTags.Items.ENI_UPGRADES_ELITE).addTag(RagiumCommonTags.Items.CIRCUITS_ELITE)

        // Other
        tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(*RagiumCommonTags.Items.BEACON_PAYMENTS)

        tag(ItemTags.MEAT).addTag(RagiumCommonTags.Items.INGOTS_MEAT).addTag(RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
        tag(ItemTags.PIGLIN_LOVED)
            .addTag(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .addItem(RagiumItems.FEVER_CHERRY)

        tag(RagiumModTags.Items.WIP)
            .addItem(RagiumItems.BOTTLED_BEE)
            .addItem(RagiumItems.EXP_BERRIES)
    }

    private fun curios() {
        tag(CuriosTags.CHARM)
            .addItem(RagiumItems.ADVANCED_RAGI_MAGNET)
            .addItem(RagiumItems.RAGI_LANTERN)
            .addItem(RagiumItems.RAGI_MAGNET)
    }

    private fun pneumatic() {
        tag(PneumaticCraftTags.Items.PLASTIC_SHEETS)
            .addOptionalTag(RagiumCommonTags.Items.PLASTICS)
            .addOptionalTag(RagiumCommonTags.Items.PLATES_PLASTIC)
    }
}
