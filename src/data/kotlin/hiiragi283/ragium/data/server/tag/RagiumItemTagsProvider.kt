package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.api.util.tool.HTArmorVariant
import hiiragi283.ragium.api.util.tool.HTToolVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTColorMaterial
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
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
import top.theillusivec4.curios.api.CuriosTags
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.common.tag.ModTags
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
        copy()

        material(builder)
        food(builder)
        categories(builder)

        curios(builder)
        pneumatic(builder)
    }

    //    Copy    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    private fun copy() {
        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
        copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK)
        copy(RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE, RagiumCommonTags.Items.ORES_IN_GROUND_END_STONE)

        copy(HTBlockMaterialVariant.ORE, RagiumMaterialType.RAGINITE)
        copy(HTBlockMaterialVariant.ORE, RagiumMaterialType.RAGI_CRYSTAL)
        copy(HTBlockMaterialVariant.ORE, RagiumMaterialType.CRIMSON_CRYSTAL)
        copy(HTBlockMaterialVariant.ORE, RagiumMaterialType.WARPED_CRYSTAL)
        copy(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)

        RagiumBlocks.MATERIALS.rowKeys.forEach(::copy)

        RagiumBlocks.MATERIALS.forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, _) ->
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

    private fun copy(variant: HTMaterialVariant.BlockTag) {
        if (!variant.canGenerateTag()) return
        copy(variant.blockCommonTag!!, variant.itemCommonTag!!)
    }

    private fun copy(variant: HTMaterialVariant.BlockTag, material: HTMaterialType) {
        if (!variant.canGenerateTag()) return
        copy(variant.blockTagKey(material), variant.itemTagKey(material))
    }

    private fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Material    //

    private fun material(builder: HTTagBuilder<Item>) {
        materialTable(builder, RagiumItems.MATERIALS)

        builder.addItem(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL, Items.COAL)
        builder.addItem(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.CHARCOAL, Items.CHARCOAL)

        val coalCoke: TagKey<Item> = HTItemMaterialVariant.FUEL.itemTagKey(RagiumMaterialType.COAL_COKE)
        builder.addTag(HTItemMaterialVariant.FUEL.itemCommonTag, coalCoke)
        builder.addTag(coalCoke, commonId(RagiumConst.COAL_COKE), HTTagBuilder.DependType.OPTIONAL)
        // Mekanism Addon
        materialTable(builder, RagiumMekanismAddon.MATERIAL_ITEMS)
    }

    private fun materialTable(builder: HTTagBuilder<Item>, table: HTTable<HTMaterialVariant, HTMaterialType, HTDeferredItem<*>>) {
        table.forEach { (variant: HTMaterialVariant, material: HTMaterialType, item: HTDeferredItem<*>) ->
            if (variant is HTMaterialVariant.ItemTag) {
                builder.addItem(variant, material, item)

                if (variant == HTItemMaterialVariant.GEM || variant == HTItemMaterialVariant.INGOT) {
                    tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(variant.itemTagKey(material))
                }
            }
        }
    }

    //    Foods    //

    private fun food(builder: HTTagBuilder<Item>) {
        // Crop
        builder.addItem(Tags.Items.CROPS, RagiumCommonTags.Items.CROPS_WARPED_WART, RagiumBlocks.WARPED_WART)
        // Food
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_CHOCOLATE)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.JAMS)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.INGOTS_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.AMBROSIA)
        builder.add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM_SODA)
        builder.add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        builder.add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_SLICE)
        builder.addItem(Tags.Items.FOODS, RagiumBlocks.WARPED_WART)

        builder.addItem(Tags.Items.FOODS_BERRY, RagiumBlocks.EXP_BERRIES)
        builder.add(Tags.Items.FOODS_GOLDEN, RagiumItems.FEVER_CHERRY)

        builder.addItem(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_APPLE, Items.APPLE)

        builder.addTag(Tags.Items.FOODS_FRUIT, RagiumCommonTags.Items.FOODS_CHERRY)
        builder.add(Tags.Items.FOODS_FRUIT, RagiumItems.FEVER_CHERRY)
        builder.addItem(
            RagiumCommonTags.Items.FOODS_CHERRY,
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            RagiumItems.RAGI_CHERRY,
        )

        builder.addTag(RagiumCommonTags.Items.FOODS_CHOCOLATE, RagiumCommonTags.Items.INGOTS_CHOCOLATE)
        // Delight
        builder.addItem(
            RagiumCommonTags.Items.FOODS_CHERRY,
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            RagiumDelightAddon.RAGI_CHERRY_PULP,
        )

        builder.addItem(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_PIE)
        builder.addItem(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCk)

        builder.addItem(RagiumCommonTags.Items.JAMS, RagiumCommonTags.Items.JAMS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_JAM)
        builder.addItem(ModTags.MEALS, RagiumDelightAddon.RAGI_CHERRY_TOAST)
        builder.addItem(ModTags.FEASTS, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCk)
    }

    //    Categories    //

    private fun categories(builder: HTTagBuilder<Item>) {
        builder.add(RagiumModTags.Items.IGNORED_IN_RECIPES, RagiumItems.SLOT_COVER)

        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, ItemTags.SMELTS_TO_GLASS)

        builder.addTag(
            RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
            HTItemMaterialVariant.DUST.itemTagKey(RagiumMaterialType.CINNABAR),
        )
        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, ItemTags.SOUL_FIRE_BASE_BLOCKS)

        // Armors
        RagiumItems.ARMORS.forEach { (variant: HTArmorVariant, _, item: HTDeferredItem<*>) ->
            builder.add(variant.tagKey, item)
        }
        // Tools
        RagiumItems.TOOLS.forEach { (variant: HTToolVariant, _, item: HTDeferredItem<*>) ->
            builder.add(variant.tagKey, item)
        }

        builder.add(Tags.Items.TOOLS_WRENCH, RagiumItems.WRENCH)

        builder.add(RagiumModTags.Items.TOOLS_DRILL, RagiumItems.DRILL)

        builder.add(CommonTags.TOOLS_KNIFE, RagiumDelightAddon.RAGI_ALLOY_KNIFE)
        builder.add(CommonTags.TOOLS_KNIFE, RagiumDelightAddon.RAGI_CRYSTAL_KNIFE)
        builder.add(ModTags.KNIVES, RagiumDelightAddon.RAGI_ALLOY_KNIFE)
        builder.add(ModTags.KNIVES, RagiumDelightAddon.RAGI_CRYSTAL_KNIFE)

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
        for ((color: HTColorMaterial, block: ItemLike) in RagiumBlocks.LED_BLOCKS) {
            builder.addItem(color.dyedTag, block)
        }
        // Parts
        builder.add(RagiumCommonTags.Items.SILICON, RagiumItems.SILICON)
        builder.add(Tags.Items.LEATHERS, RagiumItems.SYNTHETIC_LEATHER)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.RESIN)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.STRINGS, RagiumItems.SYNTHETIC_FIBER)

        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.GHAST_TEAR)
        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.PHANTOM_MEMBRANE)
        builder.addItem(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, Items.WIND_CHARGE)

        builder.add(RagiumModTags.Items.POLYMER_RESIN, RagiumItems.POLYMER_RESIN)
        builder.addOptional(RagiumModTags.Items.POLYMER_RESIN, RagiumConst.ORITECH, "polymer_resin")

        val plastics: TagKey<Item> = itemTagKey(commonId("plastic"))
        builder.add(plastics, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, HTItemMaterialVariant.PLATE.itemTagKey(RagiumMaterialType.PLASTIC))
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
            HTItemMaterialVariant.INGOT.itemTagKey(RagiumMaterialType.ADVANCED_RAGI_ALLOY),
        )
        builder.add(ItemTags.PIGLIN_LOVED, RagiumItems.FEVER_CHERRY)
        // WIP
        builder.add(RagiumModTags.Items.WIP, RagiumItems.BOTTLED_BEE)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.DRILL)
    }

    //    Integration    //

    private fun curios(builder: HTTagBuilder<Item>) {
        builder.add(CuriosTags.CHARM, RagiumItems.ADVANCED_MAGNET)
        builder.add(CuriosTags.CHARM, RagiumItems.DYNAMIC_LANTERN)
        builder.add(CuriosTags.CHARM, RagiumItems.MAGNET)
    }

    private fun pneumatic(builder: HTTagBuilder<Item>) {
        builder.add(PneumaticCraftTags.Items.PLASTIC_SHEETS, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
    }

    //    Extensions    //

    private fun HTTagBuilder<Item>.addItem(tagKey: TagKey<Item>, item: ItemLike): HTTagBuilder<Item> = apply {
        add(tagKey, item.asItemHolder().idOrThrow)
    }

    private fun HTTagBuilder<Item>.addItem(parent: TagKey<Item>, child: TagKey<Item>, item: ItemLike) =
        addTag(parent, child).addItem(child, item)

    private fun HTTagBuilder<Item>.addItem(
        variant: HTMaterialVariant.ItemTag,
        material: HTMaterialType,
        item: ItemLike,
    ): HTTagBuilder<Item> {
        val itemCommonTag: TagKey<Item> = variant.itemCommonTag ?: return this
        val tagKey: TagKey<Item> = variant.itemTagKey(material)
        return addItem(itemCommonTag, tagKey, item)
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
