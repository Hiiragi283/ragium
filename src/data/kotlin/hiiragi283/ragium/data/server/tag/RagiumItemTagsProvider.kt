package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addHolder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.tags.MekanismTags
import net.minecraft.core.Holder
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
    private fun IntrinsicTagAppender<Item>.addItem(vararg items: ItemLike): IntrinsicTagAppender<Item> = apply {
        items.map(ItemLike::asItem).map(::add)
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
        fun register(family: HTMaterialFamily) {
            if (family.isVanilla) return
            for ((variant: HTMaterialFamily.Variant, entry: HTMaterialFamily.Entry) in family.variantMap) {
                val (tagKey: TagKey<Item>, holder: Holder<Item>) = entry
                tag(variant.commonTag).addTag(tagKey)
                tag(tagKey).addItem(holder.value())
            }
        }

        register(RagiumMaterialFamilies.RAGI_CRYSTAL)
        register(RagiumMaterialFamilies.CRIMSON_CRYSTAL)
        register(RagiumMaterialFamilies.WARPED_CRYSTAL)
        register(RagiumMaterialFamilies.ELDRITCH_PEARL)

        register(RagiumMaterialFamilies.RAGI_ALLOY)
        register(RagiumMaterialFamilies.ADVANCED_RAGI_ALLOY)
        register(RagiumMaterialFamilies.AZURE_STEEL)
        register(RagiumMaterialFamilies.DEEP_STEEL)

        register(RagiumMaterialFamilies.CHOCOLATE)
        register(RagiumMaterialFamilies.MEAT)
        register(RagiumMaterialFamilies.COOKED_MEAT)

        // Dusts
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_ASH, RagiumItems.ASH_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_CINNABAR, RagiumItems.CINNABAR_DUST)
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_OBSIDIAN, RagiumItems.OBSIDIAN_DUST)
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
            .addTags(
                RagiumCommonTags.Items.ENRICHED_AZURE,
                RagiumCommonTags.Items.ENRICHED_RAGINITE,
            )
    }

    private fun foods() {
        addItem(Tags.Items.CROPS, RagiumCommonTags.Items.CROPS_WARPED_WART, RagiumItems.WARPED_WART)

        tag(Tags.Items.FOODS)
            .addTags(
                RagiumCommonTags.Items.FOODS_CHOCOLATE,
                RagiumCommonTags.Items.FOODS_JAMS,
                RagiumCommonTags.Items.INGOTS_COOKED_MEAT,
                RagiumCommonTags.Items.INGOTS_MEAT,
            ).addItem(
                RagiumItems.AMBROSIA,
                RagiumItems.CANNED_COOKED_MEAT,
                RagiumItems.FEVER_CHERRY,
                RagiumItems.ICE_CREAM,
                RagiumItems.ICE_CREAM_SODA,
                RagiumItems.MEAT_INGOT,
                RagiumItems.MELON_PIE,
                RagiumItems.SWEET_BERRIES_CAKE_SLICE,
                RagiumItems.WARPED_WART,
            )

        tag(Tags.Items.FOODS_BERRY).addItem(RagiumItems.EXP_BERRIES)
        tag(Tags.Items.FOODS_GOLDEN).addItem(RagiumItems.FEVER_CHERRY)

        tag(Tags.Items.FOODS_FRUIT)
            .addTag(RagiumCommonTags.Items.FOODS_CHERRY)
            .addItem(RagiumItems.FEVER_CHERRY)
        tag(RagiumCommonTags.Items.FOODS_CHERRY).addTag(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
        tag(RagiumCommonTags.Items.FOODS_RAGI_CHERRY).addItem(RagiumItems.RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP)

        tag(RagiumCommonTags.Items.FOODS_JAMS).addTag(RagiumCommonTags.Items.JAMS_RAGI_CHERRY)
        tag(RagiumCommonTags.Items.JAMS_RAGI_CHERRY).addItem(RagiumItems.RAGI_CHERRY_JAM)

        tag(RagiumCommonTags.Items.FOODS_CHOCOLATE).addTag(RagiumCommonTags.Items.INGOTS_CHOCOLATE)
    }

    private fun categories() {
        tag(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(
                Items.GHAST_TEAR,
                Items.PHANTOM_MEMBRANE,
                Items.WIND_CHARGE,
            )

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .addTags(ItemTags.SMELTS_TO_GLASS)

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .addTags(
                RagiumCommonTags.Items.DUSTS_CINNABAR,
                ItemTags.SOUL_FIRE_BASE_BLOCKS,
            )

        // Armors
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).addItem(RagiumItems.AZURE_STEEL_HELMET)
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).addItem(RagiumItems.AZURE_STEEL_CHESTPLATE)
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE).addItem(RagiumItems.AZURE_STEEL_LEGGINGS)
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).addItem(RagiumItems.AZURE_STEEL_BOOTS)
        // Tools
        tag(ItemTags.AXES).addItem(RagiumItems.AZURE_STEEL_TOOLS.axeItem)
        tag(ItemTags.HOES).addItem(RagiumItems.AZURE_STEEL_TOOLS.hoeItem)
        tag(ItemTags.PICKAXES).addItem(RagiumItems.AZURE_STEEL_TOOLS.pickaxeItem)
        tag(ItemTags.SHOVELS).addItem(RagiumItems.AZURE_STEEL_TOOLS.shovelItem)
        tag(ItemTags.SWORDS).addItem(RagiumItems.AZURE_STEEL_TOOLS.swordItem)

        tag(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER).addItem(
            RagiumItems.RAGI_ALLOY_HAMMER,
            RagiumItems.AZURE_STEEL_TOOLS.hammerItem,
        )
        tag(Tags.Items.TOOLS_WRENCH).addItem(
            RagiumItems.RAGI_ALLOY_HAMMER,
            RagiumItems.AZURE_STEEL_TOOLS.hammerItem,
        )

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
        // Circuits
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_BASIC, RagiumItems.BASIC_CIRCUIT)
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_ADVANCED, RagiumItems.ADVANCED_CIRCUIT)
        addItem(RagiumCommonTags.Items.CIRCUITS, RagiumCommonTags.Items.CIRCUITS_ELITE, RagiumItems.CRYSTAL_PROCESSOR)

        tag(RagiumModTags.Items.ENI_UPGRADES)
            .addTags(
                RagiumModTags.Items.ENI_UPGRADES_BASIC,
                RagiumModTags.Items.ENI_UPGRADES_ADVANCED,
                RagiumModTags.Items.ENI_UPGRADES_ELITE,
            )
        tag(RagiumModTags.Items.ENI_UPGRADES_BASIC).addTag(RagiumCommonTags.Items.CIRCUITS_BASIC)
        tag(RagiumModTags.Items.ENI_UPGRADES_ADVANCED).addTag(RagiumCommonTags.Items.CIRCUITS_ADVANCED)
        tag(RagiumModTags.Items.ENI_UPGRADES_ELITE).addTag(RagiumCommonTags.Items.CIRCUITS_ELITE)

        // Other
        tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(*RagiumCommonTags.Items.BEACON_PAYMENTS)

        tag(ItemTags.MEAT).addTags(RagiumCommonTags.Items.INGOTS_MEAT, RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
        tag(ItemTags.PIGLIN_LOVED)
            .addTag(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .addItem(RagiumItems.FEVER_CHERRY)

        tag(RagiumModTags.Items.WIP)
            .addTags(
                RagiumCommonTags.Items.INGOTS_DEEP_STEEL,
                RagiumCommonTags.Items.STORAGE_BLOCKS_DEEP_STEEL,
            ).addHolder(
                RagiumItems.ELDER_HEART,
                RagiumItems.BOTTLED_BEE,
                RagiumItems.EXP_BERRIES,
                RagiumItems.LED,
                RagiumItems.SOLAR_PANEL,
                RagiumItems.STONE_BOARD,
                RagiumItems.CRYSTAL_PROCESSOR,
            )
    }

    private fun curios() {
        tag(CuriosTags.CHARM).addItem(
            RagiumItems.EXP_MAGNET,
            RagiumItems.ITEM_MAGNET,
            RagiumItems.RAGI_LANTERN,
        )
    }

    private fun pneumatic() {
        tag(PneumaticCraftTags.Items.PLASTIC_SHEETS)
            .addOptionalTags(
                RagiumCommonTags.Items.PLASTICS,
                RagiumCommonTags.Items.PLATES_PLASTIC,
            )
    }
}
