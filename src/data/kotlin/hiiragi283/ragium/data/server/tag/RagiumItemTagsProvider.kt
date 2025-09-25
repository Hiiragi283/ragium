package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.toRowTableBy
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.accessory.HTAccessorySlot
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTColorMaterial
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
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
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
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

        accessories(builder)
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
        for (material: HTMaterialType in RagiumBlockTagsProvider.VANILLA_STORAGE_BLOCKS.keys) {
            copy(HTBlockMaterialVariant.STORAGE_BLOCK, material)
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
        materialTable(builder, RagiumItems.CIRCUITS.toRowTableBy(HTItemMaterialVariant.CIRCUIT))

        builder.addMaterial(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.COAL, HTHolderLike.fromItem(Items.COAL))
        builder.addMaterial(HTItemMaterialVariant.FUEL, HTVanillaMaterialType.CHARCOAL, HTHolderLike.fromItem(Items.CHARCOAL))

        val coalCoke: TagKey<Item> = HTItemMaterialVariant.FUEL.itemTagKey(RagiumMaterialType.COAL_COKE)
        builder.addTag(HTItemMaterialVariant.FUEL.itemCommonTag, coalCoke)
        builder.addTag(coalCoke, commonId(RagiumConst.COAL_COKE), HTTagBuilder.DependType.OPTIONAL)

        builder.addMaterial(HTItemMaterialVariant.GEM, HTVanillaMaterialType.ECHO, HTHolderLike.fromItem(Items.ECHO_SHARD))
        // Mekanism Addon
        materialTable(builder, RagiumMekanismAddon.MATERIAL_ITEMS)
    }

    companion object {
        @JvmField
        val MATERIAL_TAG: Map<HTMaterialVariant.ItemTag, TagKey<Item>> = mapOf(
            HTItemMaterialVariant.GEM to ItemTags.BEACON_PAYMENT_ITEMS,
            HTItemMaterialVariant.INGOT to ItemTags.BEACON_PAYMENT_ITEMS,
            HTItemMaterialVariant.FUEL to ItemTags.COALS,
        )
    }

    private fun materialTable(
        builder: HTTagBuilder<Item>,
        table: HTTable<HTMaterialVariant.ItemTag, out HTMaterialType, out HTHolderLike>,
    ) {
        table.forEach { (variant: HTMaterialVariant.ItemTag, material: HTMaterialType, item: HTHolderLike) ->
            builder.addMaterial(variant, material, item)
            val customTag: TagKey<Item> = MATERIAL_TAG[variant] ?: return@forEach
            builder.addTag(customTag, variant.itemTagKey(material))
        }
    }

    //    Foods    //

    private fun food(builder: HTTagBuilder<Item>) {
        fun ingot(material: HTMaterialType): TagKey<Item> = HTItemMaterialVariant.INGOT.itemTagKey(material)

        // Crop
        builder.add(Tags.Items.CROPS, RagiumCommonTags.Items.CROPS_WARPED_WART, RagiumBlocks.WARPED_WART)
        // Food
        builder.addTag(Tags.Items.FOODS, ingot(RagiumMaterialType.COOKED_MEAT))
        builder.addTag(Tags.Items.FOODS, ingot(RagiumMaterialType.MEAT))
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_CHOCOLATE)
        builder.addTag(Tags.Items.FOODS, RagiumCommonTags.Items.JAMS)
        builder.add(Tags.Items.FOODS, RagiumItems.AMBROSIA)
        builder.add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM_SODA)
        builder.add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        builder.add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_SLICE)
        builder.add(Tags.Items.FOODS, RagiumBlocks.WARPED_WART)

        builder.add(Tags.Items.FOODS_BERRY, RagiumBlocks.EXP_BERRIES)
        builder.add(Tags.Items.FOODS_GOLDEN, RagiumItems.FEVER_CHERRY)

        builder.add(Tags.Items.FOODS, RagiumCommonTags.Items.FOODS_APPLE, HTHolderLike.fromItem(Items.APPLE))

        builder.addTag(Tags.Items.FOODS_FRUIT, RagiumCommonTags.Items.FOODS_CHERRY)
        builder.add(Tags.Items.FOODS_FRUIT, RagiumItems.FEVER_CHERRY)
        builder.add(
            RagiumCommonTags.Items.FOODS_CHERRY,
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            RagiumItems.RAGI_CHERRY,
        )

        builder.addTag(RagiumCommonTags.Items.FOODS_CHOCOLATE, ingot(RagiumMaterialType.CHOCOLATE))

        builder.addTag(ItemTags.MEAT, ingot(RagiumMaterialType.COOKED_MEAT))
        builder.addTag(ItemTags.MEAT, ingot(RagiumMaterialType.MEAT))

        builder.addTag(RagiumModTags.Items.RAW_MEAT, Tags.Items.FOODS_RAW_MEAT)
        builder.addTag(RagiumModTags.Items.RAW_MEAT, Tags.Items.FOODS_RAW_FISH)
        builder.add(RagiumModTags.Items.RAW_MEAT, HTHolderLike.fromItem(Items.ROTTEN_FLESH))
        // Delight
        builder.add(RagiumCommonTags.Items.FOODS_CHERRY, RagiumCommonTags.Items.FOODS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP)

        builder.add(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_PIE)
        builder.add(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)

        builder.add(RagiumCommonTags.Items.JAMS, RagiumCommonTags.Items.JAMS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_JAM)
        builder.add(ModTags.MEALS, RagiumDelightAddon.RAGI_CHERRY_TOAST)
        builder.add(ModTags.FEASTS, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
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
        for ((variant: HTArmorVariant, armor: HTHolderLike) in RagiumItems.AZURE_ARMORS) {
            builder.add(variant.tagKey, armor)
        }
        for ((variant: HTArmorVariant, armor: HTHolderLike) in RagiumItems.DEEP_ARMORS) {
            builder.add(variant.tagKey, armor)
        }
        // Tools
        RagiumItems.TOOLS.forEach { (variant: HTToolVariant, _, item: HTHolderLike) ->
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
            builder.add(Tags.Items.BUCKETS, content.bucketTag, HTHolderLike.fromItem(content.getBucket()))
        }
        // LED
        for ((color: HTColorMaterial, block: HTHolderLike) in RagiumBlocks.LED_BLOCKS) {
            builder.add(color.dyedTag, block)
        }
        // Parts
        builder.add(Tags.Items.LEATHERS, RagiumItems.SYNTHETIC_LEATHER)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.RESIN)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.STRINGS, RagiumItems.SYNTHETIC_FIBER)

        listOf(Items.GHAST_TEAR, Items.PHANTOM_MEMBRANE, Items.WIND_CHARGE)
            .map(HTHolderLike::fromItem)
            .forEach { holder: HTHolderLike -> builder.add(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, holder) }

        builder.add(RagiumModTags.Items.POLYMER_RESIN, RagiumItems.POLYMER_RESIN)
        builder.addOptional(RagiumModTags.Items.POLYMER_RESIN, RagiumConst.ORITECH, "polymer_resin")

        val plastics: TagKey<Item> = builder.createTag(commonId("plastic"))
        builder.add(plastics, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, HTItemMaterialVariant.PLATE.itemTagKey(RagiumMaterialType.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, plastics, HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(
            RagiumModTags.Items.PLASTICS,
            PneumaticCraftTags.Items.PLASTIC_SHEETS,
            HTTagBuilder.DependType.OPTIONAL,
        )
        // Other
        builder.addTag(
            ItemTags.PIGLIN_LOVED,
            HTItemMaterialVariant.INGOT.itemTagKey(RagiumMaterialType.ADVANCED_RAGI_ALLOY),
        )
        builder.add(ItemTags.PIGLIN_LOVED, RagiumItems.FEVER_CHERRY)
        // WIP
        builder.add(RagiumModTags.Items.WIP, RagiumDelightAddon.RAGI_CHERRY_TOAST)
        builder.add(RagiumModTags.Items.WIP, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.BOTTLED_BEE)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.DRILL)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.GREEN_PELLET)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.SLOT_COVER)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.SOLAR_PANEL)
    }

    //    Integration    //

    private fun accessories(builder: HTTagBuilder<Item>) {
        builder.addAccessory(HTAccessorySlot.BACK, RagiumItems.ECHO_STAR)
        builder.addAccessory(HTAccessorySlot.BELT, RagiumItems.POTION_BUNDLE)
        builder.addAccessory(HTAccessorySlot.BELT, RagiumItems.UNIVERSAL_BUNDLE)
        builder.addAccessory(HTAccessorySlot.CHARM, RagiumItems.ADVANCED_MAGNET)
        builder.addAccessory(HTAccessorySlot.CHARM, RagiumItems.DYNAMIC_LANTERN)
        builder.addAccessory(HTAccessorySlot.CHARM, RagiumItems.MAGNET)
        builder.addAccessory(HTAccessorySlot.FACE, RagiumItems.NIGHT_VISION_GOGGLES)
    }

    private fun pneumatic(builder: HTTagBuilder<Item>) {
        builder.add(PneumaticCraftTags.Items.PLASTIC_SHEETS, RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
    }

    //    Extensions    //

    private fun HTTagBuilder<Item>.add(parent: TagKey<Item>, child: TagKey<Item>, holder: HTHolderLike) =
        addTag(parent, child).add(child, holder)

    private fun HTTagBuilder<Item>.addMaterial(
        variant: HTMaterialVariant.ItemTag,
        material: HTMaterialType,
        holder: HTHolderLike,
    ): HTTagBuilder<Item> {
        val itemCommonTag: TagKey<Item> = variant.itemCommonTag ?: return this
        val tagKey: TagKey<Item> = variant.itemTagKey(material)
        return add(itemCommonTag, tagKey, holder)
    }

    private fun HTTagBuilder<Item>.addAccessory(slot: HTAccessorySlot, holder: HTHolderLike): HTTagBuilder<Item> = add(slot.slotTag, holder)

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
