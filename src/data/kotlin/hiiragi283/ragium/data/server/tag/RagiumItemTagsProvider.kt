package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.commonId
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
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTArmorVariant
import hiiragi283.ragium.util.variant.HTColorVariant
import hiiragi283.ragium.util.variant.HTToolVariant
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.tags.MekanismTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagBuilder
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
    private val blockTags: CompletableFuture<TagLookup<Block>>,
    helper: ExistingFileHelper,
) : HTTagsProvider<Item>(
        output,
        Registries.ITEM,
        provider,
        helper,
    ) {
    override fun addTags(builder: HTTagBuilder<Item>) {
        copy(builder)

        material(builder)
        food(builder)
        categories(builder)

        curios(builder)
        pneumatic(builder)
    }

    //    Copy    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    private fun copy(builder: HTTagBuilder<Item>) {
        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
        copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK)
        copy(RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE, RagiumCommonTags.Items.ORES_IN_GROUND_END_STONE)

        copy(HTMaterialVariant.ORE, RagiumMaterialType.RAGINITE)
        copy(HTMaterialVariant.ORE, RagiumMaterialType.RAGI_CRYSTAL)
        copy(HTMaterialVariant.ORE, RagiumMaterialType.CRIMSON_CRYSTAL)
        copy(HTMaterialVariant.ORE, RagiumMaterialType.WARPED_CRYSTAL)
        copy(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)

        RagiumBlocks.MATERIALS.rowKeys.forEach(::copy)

        RagiumBlocks.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, _) ->
            copy(variant, material)
        }

        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
        copy(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)

        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)
        copy(RagiumModTags.Blocks.LED_BLOCKS, RagiumModTags.Items.LED_BLOCKS)

        copy(RagiumModTags.Blocks.WIP, RagiumModTags.Items.WIP)
    }

    private fun copy(variant: HTMaterialVariant) {
        if (!variant.canGenerateTag()) return
        copy(variant.blockCommonTag!!, variant.itemCommonTag!!)
    }

    private fun copy(variant: HTMaterialVariant, material: HTMaterialType) {
        if (!variant.canGenerateTag()) return
        copy(variant.blockTagKey(material), variant.itemTagKey(material))
    }

    private fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Material    //

    private fun material(builder: HTTagBuilder<Item>) {
        RagiumItems.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, item: DeferredItem<*>) ->
            builder.addItem(variant, material, item)
            if (variant == HTMaterialVariant.GEM || variant == HTMaterialVariant.INGOT) {
                tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(variant.itemTagKey(material))
            }
        }
        builder.addItem(HTMaterialVariant.DUST, RagiumMaterialType.MEAT, RagiumItems.MINCED_MEAT)

        builder.addItem(HTMaterialVariant.GEAR, RagiumMaterialType.ELDRITCH_PEARL, RagiumItems.ELDRITCH_GEAR)

        builder.addItem(HTMaterialVariant.FUEL, HTVanillaMaterialType.COAL, Items.COAL)
        builder.addItem(HTMaterialVariant.FUEL, HTVanillaMaterialType.CHARCOAL, Items.CHARCOAL)

        val coalCoke: TagKey<Item> = HTMaterialVariant.FUEL.itemTagKey(RagiumMaterialType.COAL_COKE)
        builder.addTag(HTMaterialVariant.FUEL.itemCommonTag!!, coalCoke)
        builder.addTag(coalCoke, commonId(RagiumConst.COAL_COKE), HTTagBuilder.DependType.OPTIONAL)
        // Mekanism Addon
        builder.addItem(
            MekanismTags.Items.ENRICHED,
            RagiumModTags.Items.ENRICHED_AZURE,
            RagiumMekanismAddon.ITEM_ENRICHED_AZURE,
        )
        builder.addItem(
            MekanismTags.Items.ENRICHED,
            RagiumModTags.Items.ENRICHED_RAGINITE,
            RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE,
        )
    }

    //    Foods    //

    private fun food(builder: HTTagBuilder<Item>) {
        // Crop
        builder.addItem(Tags.Items.CROPS, RagiumCommonTags.Items.CROPS_WARPED_WART, RagiumItems.WARPED_WART)
        // Food
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_CHOCOLATE)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_JAMS)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.INGOTS_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.AMBROSIA)
        builder.add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM_SODA)
        builder.add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        builder.add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_SLICE)
        builder.add(Tags.Items.FOODS, RagiumItems.WARPED_WART)

        builder.add(Tags.Items.FOODS_BERRY, RagiumItems.EXP_BERRIES)
        builder.add(Tags.Items.FOODS_GOLDEN, RagiumItems.FEVER_CHERRY)

        builder.addTag(Tags.Items.FOODS_FRUIT, RagiumCommonTags.Items.FOODS_CHERRY)
        builder.add(Tags.Items.FOODS_FRUIT, RagiumItems.FEVER_CHERRY)
        builder.addTag(RagiumCommonTags.Items.FOODS_CHERRY, RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
        builder.add(RagiumCommonTags.Items.FOODS_RAGI_CHERRY, RagiumItems.RAGI_CHERRY)
        builder.add(RagiumCommonTags.Items.FOODS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP)

        builder.addItem(
            RagiumCommonTags.Items.FOODS_JAMS,
            RagiumCommonTags.Items.JAMS_RAGI_CHERRY,
            RagiumItems.RAGI_CHERRY_JAM,
        )

        builder.addTag(RagiumCommonTags.Items.FOODS_CHOCOLATE, RagiumCommonTags.Items.INGOTS_CHOCOLATE)
    }

    //    Categories    //

    private fun categories(builder: HTTagBuilder<Item>) {
        builder.add(RagiumModTags.Items.IGNORED_IN_RECIPES, RagiumItems.SLOT_COVER)

        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, ItemTags.SMELTS_TO_GLASS)

        builder.addTag(
            RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
            HTMaterialVariant.DUST.itemTagKey(RagiumMaterialType.CINNABAR),
        )
        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, ItemTags.SOUL_FIRE_BASE_BLOCKS)

        // Armors
        RagiumItems.ARMORS.forEach { (variant: HTArmorVariant, _, item: DeferredItem<*>) ->
            builder.add(variant.tagKey, item)
        }
        // Tools
        RagiumItems.TOOLS.forEach { (variant: HTToolVariant, _, item: DeferredItem<*>) ->
            builder.add(variant.tagKey, item)
        }
        for (hammer: DeferredItem<*> in RagiumItems.TOOLS.rowValues(HTToolVariant.HAMMER)) {
            builder.add(Tags.Items.TOOLS_WRENCH, hammer)
        }

        builder.add(RagiumModTags.Items.TOOLS_DRILL, RagiumItems.DRILL)

        fun setupTool(tagKey: TagKey<Item>) {
            builder.addTag(ItemTags.BREAKS_DECORATED_POTS, tagKey)
            builder.addTag(ItemTags.DURABILITY_ENCHANTABLE, tagKey)
            builder.addTag(ItemTags.MINING_ENCHANTABLE, tagKey)
            builder.addTag(ItemTags.MINING_LOOT_ENCHANTABLE, tagKey)
            builder.addTag(Tags.Items.TOOLS, tagKey)
        }

        setupTool(RagiumModTags.Items.TOOLS_DRILL)
        setupTool(RagiumModTags.Items.TOOLS_HAMMER)
        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            builder.addItem(Tags.Items.BUCKETS, content.bucketTag, content.getBucket())
        }
        // LED
        for ((color: HTColorVariant, block: ItemLike) in RagiumBlocks.LED_BLOCKS) {
            builder.addItem(color.dyedTag, block)
        }
        // Parts
        builder.add(RagiumCommonTags.Items.SILICON, RagiumItems.SILICON)
        builder.add(Tags.Items.LEATHERS, RagiumItems.SYNTHETIC_LEATHER)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.STRINGS, RagiumItems.SYNTHETIC_FIBER)

        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.GHAST_TEAR)
        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.PHANTOM_MEMBRANE)
        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.WIND_CHARGE)

        builder.add(RagiumModTags.Items.POLYMER_RESIN, RagiumItems.POLYMER_RESIN)
        builder.addOptional(RagiumModTags.Items.POLYMER_RESIN, RagiumConst.ORITECH, "polymer_resin")

        val plastics: TagKey<Item> = itemTagKey(commonId("plastic"))
        builder.add(plastics, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, HTMaterialVariant.PLATE.itemTagKey(RagiumMaterialType.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, plastics, HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(
            RagiumModTags.Items.PLASTICS,
            PneumaticCraftTags.Items.PLASTIC_SHEETS,
            HTTagBuilder.DependType.OPTIONAL,
        )
        // Other
        builder.addTag(ItemTags.MEAT, RagiumCommonTags.Items.INGOTS_MEAT)
        builder.addTag(ItemTags.MEAT, RagiumCommonTags.Items.INGOTS_COOKED_MEAT)

        builder.addTag(
            ItemTags.PIGLIN_LOVED,
            HTMaterialVariant.INGOT.itemTagKey(RagiumMaterialType.ADVANCED_RAGI_ALLOY),
        )
        builder.add(ItemTags.PIGLIN_LOVED, RagiumItems.FEVER_CHERRY)
        // WIP
        builder.add(RagiumModTags.Items.WIP, RagiumItems.BOTTLED_BEE)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.DRILL)
    }

    //    Integration    //

    private fun curios(builder: HTTagBuilder<Item>) {
        builder.add(CuriosTags.CHARM, RagiumItems.ADVANCED_RAGI_MAGNET)
        builder.add(CuriosTags.CHARM, RagiumItems.RAGI_LANTERN)
        builder.add(CuriosTags.CHARM, RagiumItems.RAGI_MAGNET)
    }

    private fun pneumatic(builder: HTTagBuilder<Item>) {
        builder.add(PneumaticCraftTags.Items.PLASTIC_SHEETS, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
    }

    //    Extensions    //

    private fun HTTagBuilder<Item>.addItem(tagKey: TagKey<Item>, item: ItemLike) {
        add(tagKey, item.asItemHolder())
    }

    private fun HTTagBuilder<Item>.addItem(parent: TagKey<Item>, child: TagKey<Item>, item: ItemLike) {
        addTag(parent, child)
        addItem(child, item)
    }

    private fun HTTagBuilder<Item>.addItem(variant: HTMaterialVariant, material: HTMaterialType, item: ItemLike) {
        val itemCommonTag: TagKey<Item> = variant.itemCommonTag ?: return
        val tagKey: TagKey<Item> = variant.itemTagKey(material)
        addItem(itemCommonTag, tagKey, item)
    }

    override fun createContentsProvider(): CompletableFuture<HolderLookup.Provider> = super
        .createContentsProvider()
        .thenCombine(blockTags) { provider: HolderLookup.Provider, lookup: TagLookup<Block> ->
            for ((blockTag: TagKey<Block>, itemTag: TagKey<Item>) in tagsToCopy) {
                val builder: TagBuilder = getOrCreateRawBuilder(itemTag)
                lookup
                    .apply(blockTag)
                    .orElseThrow { error("Missing block tag ${itemTag.location}") }
                    .build()
                    .forEach(builder::add)
            }
            provider
        }
}
