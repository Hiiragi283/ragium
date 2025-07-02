package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addHolder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import mekanism.common.tags.MekanismTags
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
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
    private fun IntrinsicTagAppender<Item>.addItem(vararg items: ItemLike) {
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
    }

    private fun copy() {
        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)

        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(RagiumBlockTags.ORES_RAGINITE, RagiumItemTags.ORES_RAGINITE)
        copy(RagiumBlockTags.ORES_RAGI_CRYSTAL, RagiumItemTags.ORES_RAGI_CRYSTAL)

        copy(RagiumBlockTags.GLASS_BLOCKS_OBSIDIAN, RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
        copy(RagiumBlockTags.GLASS_BLOCKS_QUARTZ, RagiumItemTags.GLASS_BLOCKS_QUARTZ)
        copy(RagiumBlockTags.GLASS_BLOCKS_SOUL, RagiumItemTags.GLASS_BLOCKS_SOUL)
        copy(RagiumBlockTags.LED_BLOCKS, RagiumItemTags.LED_BLOCKS)
        copy(RagiumBlockTags.OBSIDIANS_MYSTERIOUS, RagiumItemTags.OBSIDIANS_MYSTERIOUS)
        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)

        copy(RagiumBlockTags.WIP, RagiumItemTags.WIP)
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
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_ASH, RagiumItems.ASH_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_CINNABAR, RagiumItems.CINNABAR_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_OBSIDIAN, RagiumItems.OBSIDIAN_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_RAGINITE, RagiumItems.RAGINITE_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_SALTPETER, RagiumItems.SALTPETER_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_SULFUR, RagiumItems.SULFUR_DUST)
        addItem(Tags.Items.DUSTS, RagiumItemTags.DUSTS_WOOD, RagiumItems.SAWDUST)
        // Mekanism Addon
        tag(RagiumItemTags.ENRICHED_AZURE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE)
        tag(RagiumItemTags.ENRICHED_RAGINITE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE)
        tag(MekanismTags.Items.ENRICHED).addTags(RagiumItemTags.ENRICHED_AZURE, RagiumItemTags.ENRICHED_RAGINITE)
    }

    private fun foods() {
        addItem(Tags.Items.CROPS, RagiumItemTags.CROPS_WARPED_WART, RagiumItems.WARPED_WART)

        tag(Tags.Items.FOODS)
            .addTags(
                RagiumItemTags.FOODS_CHOCOLATE,
                RagiumItemTags.FOODS_JAMS,
                RagiumItemTags.INGOTS_COOKED_MEAT,
                RagiumItemTags.INGOTS_MEAT,
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
            .addTag(RagiumItemTags.FOODS_CHERRY)
            .addItem(RagiumItems.FEVER_CHERRY)
        tag(RagiumItemTags.FOODS_CHERRY).addTag(RagiumItemTags.FOODS_RAGI_CHERRY)
        tag(RagiumItemTags.FOODS_RAGI_CHERRY).addItem(RagiumItems.RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP)

        tag(RagiumItemTags.FOODS_JAMS).addTag(RagiumItemTags.JAMS_RAGI_CHERRY)
        tag(RagiumItemTags.JAMS_RAGI_CHERRY).addItem(RagiumItems.RAGI_CHERRY_JAM)

        tag(RagiumItemTags.FOODS_CHOCOLATE).addTag(RagiumItemTags.INGOTS_CHOCOLATE)
    }

    private fun categories() {
        tag(RagiumItemTags.ELDRITCH_PEARL_BINDER)
            .addItem(
                Items.GHAST_TEAR,
                Items.PHANTOM_MEMBRANE,
                Items.WIND_CHARGE,
            )

        tag(RagiumItemTags.ALLOY_SMELTER_FLUXES_BASIC)
            .addTags(ItemTags.SMELTS_TO_GLASS)

        tag(RagiumItemTags.ALLOY_SMELTER_FLUXES_ADVANCED)
            .addTags(
                RagiumItemTags.DUSTS_CINNABAR,
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

        tag(RagiumItemTags.TOOLS_FORGE_HAMMER).addItem(
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
        ).map(::tag).forEach { it.addTag(RagiumItemTags.TOOLS_FORGE_HAMMER) }

        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            addItem(Tags.Items.BUCKETS, content.bucketTag, content.bucketHolder)
        }

        // Parts
        tag(Tags.Items.SLIME_BALLS).addItem(RagiumItems.TAR)
        tag(RagiumItemTags.PAPER).addItem(Items.PAPER)
        // Circuits
        addItem(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_BASIC, RagiumItems.BASIC_CIRCUIT)
        addItem(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_ADVANCED, RagiumItems.ADVANCED_CIRCUIT)
        addItem(RagiumItemTags.CIRCUITS, RagiumItemTags.CIRCUITS_ELITE, RagiumItems.CRYSTAL_PROCESSOR)

        tag(RagiumItemTags.ENI_UPGRADES)
            .addTags(
                RagiumItemTags.ENI_UPGRADES_BASIC,
                RagiumItemTags.ENI_UPGRADES_ADVANCED,
                RagiumItemTags.ENI_UPGRADES_ELITE,
            )
        tag(RagiumItemTags.ENI_UPGRADES_BASIC).addTag(RagiumItemTags.CIRCUITS_BASIC)
        tag(RagiumItemTags.ENI_UPGRADES_ADVANCED).addTag(RagiumItemTags.CIRCUITS_ADVANCED)
        tag(RagiumItemTags.ENI_UPGRADES_ELITE).addTag(RagiumItemTags.CIRCUITS_ELITE)

        // Other
        tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(*RagiumItemTags.BEACON_PAYMENTS)

        tag(ItemTags.MEAT).addTags(RagiumItemTags.INGOTS_MEAT, RagiumItemTags.INGOTS_COOKED_MEAT)
        tag(ItemTags.PIGLIN_LOVED)
            .addTag(RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY)
            .addItem(RagiumItems.FEVER_CHERRY)

        tag(RagiumItemTags.WIP)
            .addTags(
                RagiumItemTags.INGOTS_DEEP_STEEL,
                RagiumItemTags.STORAGE_BLOCKS_DEEP_STEEL,
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
}
