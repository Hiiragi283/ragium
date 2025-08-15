package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.copyTo
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.rowValues
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.variant.HTArmorVariant
import hiiragi283.ragium.util.variant.HTToolVariant
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.tags.MekanismTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
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
    override fun addTags(provider: HolderLookup.Provider) {
        copy()

        materials()
        foods()
        categories()

        curios()
        pneumatic()
    }

    private fun copy() {
        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
        copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK)
        copy(RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE, RagiumCommonTags.Items.ORES_IN_GROUND_END_STONE)
        copy(RagiumCommonTags.Blocks.ORES_RAGINITE, RagiumCommonTags.Items.ORES_RAGINITE)
        copy(RagiumCommonTags.Blocks.ORES_RAGI_CRYSTAL, RagiumCommonTags.Items.ORES_RAGI_CRYSTAL)
        copy(RagiumCommonTags.Blocks.ORES_CRIMSON_CRYSTAL, RagiumCommonTags.Items.ORES_CRIMSON_CRYSTAL)
        copy(RagiumCommonTags.Blocks.ORES_WARPED_CRYSTAL, RagiumCommonTags.Items.ORES_WARPED_CRYSTAL)

        copy(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
        buildList {
            addAll(RagiumBlocks.MATERIALS.rowKeys.map(HTMaterialVariant::blockCommonTag))
            RagiumBlocks.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, _) ->
                add(variant.blockTagKey(material))
            }
        }.forEach(::copyTo)

        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
        copy(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)

        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)
        copy(RagiumModTags.Blocks.LED_BLOCKS, RagiumModTags.Items.LED_BLOCKS)

        copy(RagiumModTags.Blocks.WIP, RagiumModTags.Items.WIP)
    }

    //    Material    //

    private fun materials() {
        addItem(Tags.Items.DUSTS, RagiumCommonTags.Items.DUSTS_MEAT, RagiumItems.MINCED_MEAT)
        RagiumItems.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, item: DeferredItem<*>) ->
            addItem(variant.itemCommonTag, variant.itemTagKey(material), item)
        }
        addItem(RagiumCommonTags.Items.PLATES, RagiumCommonTags.Items.PLATES_PLASTIC, RagiumItems.PLASTIC_PLATE)

        // Mekanism Addon
        tag(RagiumModTags.Items.ENRICHED_AZURE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE)
        tag(RagiumModTags.Items.ENRICHED_RAGINITE).addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE)
        tag(MekanismTags.Items.ENRICHED)
            .addTag(RagiumModTags.Items.ENRICHED_AZURE)
            .addTag(RagiumModTags.Items.ENRICHED_RAGINITE)
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
        tag(RagiumModTags.Items.CIRCUIT_BOARDS)
            .addItem(RagiumItems.CIRCUIT_BOARD)
            .addOptional(ResourceLocation.fromNamespaceAndPath(RagiumConst.PNEUMATIC, "empty_pcb"))

        tag(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)

        tag(RagiumModTags.Items.IGNORED_IN_RECIPES)
            .addItem(RagiumItems.SLOT_COVER)

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .addTag(ItemTags.SMELTS_TO_GLASS)

        tag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .addTag(RagiumCommonTags.Items.DUSTS_CINNABAR)
            .addTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)

        // Armors
        RagiumItems.ARMORS.forEach { (variant: HTArmorVariant, _, item: DeferredItem<*>) ->
            tag(variant.tagKey).addItem(item)
        }

        // Tools
        RagiumItems.TOOLS.forEach { (variant: HTToolVariant, _, item: DeferredItem<*>) ->
            tag(variant.tagKey).addItem(item)
        }
        for (hammer: DeferredItem<*> in RagiumItems.TOOLS.rowValues(HTToolVariant.HAMMER)) {
            tag(Tags.Items.TOOLS_WRENCH).addItem(hammer)
        }

        tag(RagiumModTags.Items.TOOLS_DRILL)
            .addItem(RagiumItems.DRILL)

        fun setupTool(tagKey: TagKey<Item>) {
            tag(ItemTags.BREAKS_DECORATED_POTS).addTag(tagKey)
            tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(tagKey)
            tag(ItemTags.MINING_ENCHANTABLE).addTag(tagKey)
            tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(tagKey)
            tag(Tags.Items.TOOLS).addTag(tagKey)
        }

        setupTool(RagiumModTags.Items.TOOLS_DRILL)
        setupTool(RagiumModTags.Items.TOOLS_HAMMER)

        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            addItem(Tags.Items.BUCKETS, content.bucketTag, content.getBucket())
        }

        // Parts
        tag(Tags.Items.SLIME_BALLS).addItem(RagiumItems.TAR)
        tag(RagiumCommonTags.Items.PAPER).addItem(Items.PAPER)
        tag(RagiumModTags.Items.POLYMER_RESIN)
            .addItem(RagiumItems.POLYMER_RESIN)
            .addOptional(ResourceLocation.fromNamespaceAndPath(RagiumConst.ORITECH, "polymer_resin"))

        val plastics: TagKey<Item> = itemTagKey(commonId("plastic"))
        tag(plastics).addItem(RagiumItems.PLASTIC_PLATE)
        tag(RagiumModTags.Items.PLASTICS)
            .addOptionalTag(plastics)
            .addOptionalTag(RagiumCommonTags.Items.PLATES_PLASTIC)
            .addOptionalTag(PneumaticCraftTags.Items.PLASTIC_SHEETS)

        tag(Tags.Items.LEATHERS).addItem(RagiumItems.SYNTHETIC_LEATHER)
        tag(Tags.Items.STRINGS).addItem(RagiumItems.SYNTHETIC_FIBER)
        // Other
        tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(*RagiumCommonTags.Items.BEACON_PAYMENTS)

        tag(ItemTags.MEAT).addTag(RagiumCommonTags.Items.INGOTS_MEAT).addTag(RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
        tag(ItemTags.PIGLIN_LOVED)
            .addTag(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .addItem(RagiumItems.FEVER_CHERRY)

        tag(RagiumModTags.Items.WIP)
            .addItem(RagiumItems.BOTTLED_BEE)
            .addItem(RagiumItems.DRILL)
    }

    private fun curios() {
        tag(CuriosTags.CHARM)
            .addItem(RagiumItems.ADVANCED_RAGI_MAGNET)
            .addItem(RagiumItems.RAGI_LANTERN)
            .addItem(RagiumItems.RAGI_MAGNET)
    }

    private fun pneumatic() {
        tag(PneumaticCraftTags.Items.PLASTIC_SHEETS).addItem(RagiumItems.PLASTIC_PLATE)
    }

    //    Extensions    //

    fun IntrinsicTagAppender<Item>.addItem(item: ItemLike): IntrinsicTagAppender<Item> = apply {
        add(item.asItem())
    }

    private fun addItem(parent: TagKey<Item>, child: TagKey<Item>, item: ItemLike) {
        tag(parent).addTag(child)
        tag(child).addItem(item)
    }

    private fun copyTo(tagKey: TagKey<Block>) {
        copy(tagKey, tagKey.copyTo(Registries.ITEM))
    }
}
