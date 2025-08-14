package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.data.server.material.ModMaterialFamilies
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        HTMaterialFamily.instances.values.forEach(::registerFamilies)

        materials()

        oreToRaw()
        alloying()
    }

    private fun materials() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.getCompound(RagiumMaterialType.RAGI_ALLOY))
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.INGOTS_RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust("copper"),
                HTIngredientHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 2),
            ).save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', ItemTags.COALS)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.getCompound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust("gold"),
                HTIngredientHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 4),
            ).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL))
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL),
                HTIngredientHelper.gemOrDust("diamond"),
                HTIngredientHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 6),
            ).save(output)
        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.AZURE), 2)
            .mosaic4()
            .define('A', Tags.Items.GEMS_AMETHYST)
            .define('B', Tags.Items.GEMS_LAPIS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.getCompound(RagiumMaterialType.AZURE_STEEL))
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_AZURE)
            .define('B', Tags.Items.INGOTS_IRON)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.AZURE_STEEL))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.GEMS_AZURE, 2),
                HTIngredientHelper.gemOrDust("amethyst"),
                HTIngredientHelper.gemOrDust("lapis"),
            ).save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.INGOTS_AZURE_STEEL),
                HTIngredientHelper.ingotOrDust("iron"),
                HTIngredientHelper.gemOrDust(RagiumConst.AZURE, 2),
            ).save(output)
        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_WOOD)
            .define('B', RagiumItems.getDust(RagiumMaterialType.WOOD))
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL, onlyBlasting = true)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")
        // Eldritch Pearl
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL))
            .cross4()
            .define('A', RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .define('B', RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL, 9),
                HTIngredientHelper.item(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL),
                HTIngredientHelper.item(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL),
                HTIngredientHelper.item(RagiumModTags.Items.ELDRITCH_PEARL_BINDER),
            ).save(output)

        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL),
                HTIngredientHelper.fluid(RagiumFluidContents.WARPED_SAP, 1000),
                HTResultHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).save(output)

        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 1000),
                HTResultHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).saveSuffixed(output, "_alt")

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                HTIngredientHelper.item(Tags.Items.ENDER_PEARLS),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
                HTResultHelper.item(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL),
            ).save(output)
        // Deep Steel
        HTCookingRecipeBuilder
            .blasting(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL))
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumItems.DEEP_SCRAP)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .save(output)

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_DEEP_SCRAP))
            .addResult(HTResultHelper.item(RagiumItems.DEEP_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
        // Other
        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SULFUR)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SALTPETER)
            .addIngredient(HTIngredientHelper.charcoal())
            .addIngredient(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_hammer")
    }

    private fun oreToRaw() {
        // Coal
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_COAL))
            .addResult(HTResultHelper.item(Items.COAL, 2))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_SULFUR), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Copper
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_COPPER))
            .addResult(HTResultHelper.item(Tags.Items.RAW_MATERIALS_COPPER, 4))
            .addResult(HTResultHelper.item(Tags.Items.RAW_MATERIALS_GOLD), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_IRON))
            .addResult(HTResultHelper.item(Tags.Items.RAW_MATERIALS_IRON, 2))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_GOLD))
            .addResult(HTResultHelper.item(Tags.Items.RAW_MATERIALS_GOLD, 2))
            .addResult(HTResultHelper.item(Tags.Items.RAW_MATERIALS_COPPER), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_REDSTONE))
            .addResult(HTResultHelper.item(Tags.Items.DUSTS_REDSTONE, 8))
            .addResult(HTResultHelper.item(Tags.Items.DUSTS_REDSTONE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_CINNABAR, 4), 1 / 2f)
            .saveSuffixed(output, "_from_ore")
        // Raginite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_RAGINITE))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 8))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Gems
        mapOf(
            // Vanilla
            HTVanillaMaterialType.LAPIS to 8,
            HTVanillaMaterialType.QUARTZ to 4,
            HTVanillaMaterialType.DIAMOND to 2,
            HTVanillaMaterialType.EMERALD to 2,
            // Ragium
            RagiumMaterialType.RAGI_CRYSTAL to 2,
            RagiumMaterialType.CRIMSON_CRYSTAL to 2,
            RagiumMaterialType.WARPED_CRYSTAL to 2,
        ).forEach { (material: HTMaterialType, count: Int) ->
            HTItemToChancedItemRecipeBuilder
                .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE.itemTagKey(material)))
                .addResult(HTResultHelper.item(HTMaterialVariant.GEM.itemTagKey(material), count))
                .saveSuffixed(output, "_from_ore")
        }

        // Netherite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_NETHERITE_SCRAP))
            .addResult(HTResultHelper.item(Items.NETHERITE_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    private fun alloying() {
        // Netherite
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(Tags.Items.INGOTS_NETHERITE, 2),
                HTIngredientHelper.ingotOrDust("gold", 4),
                HTIngredientHelper.item(Items.NETHERITE_SCRAP, 4),
            ).save(output)
        // Deep Steel
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumCommonTags.Items.INGOTS_DEEP_STEEL, 2),
                HTIngredientHelper.ingotOrDust(RagiumConst.AZURE_STEEL, 4),
                HTIngredientHelper.item(RagiumItems.DEEP_SCRAP, 4),
            ).save(output)

        // Steel
        val steelTag: TagKey<Item> = ModMaterialFamilies.getAlloy("steel").getBaseTagKey()
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(steelTag),
                HTIngredientHelper.ingotOrDust("iron"),
                HTIngredientHelper.item(HTIngredientHelper.coal(), 2),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(steelTag),
                HTIngredientHelper.ingotOrDust("iron"),
                HTIngredientHelper.item(HTIngredientHelper.coalCoke()),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("invar").getBaseTagKey(), 3),
                HTIngredientHelper.ingotOrDust("iron", 2),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("invar").getBaseTagKey())
            .save(output)
        // Electrum
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("gold"),
                HTIngredientHelper.ingotOrDust("silver"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey())
            .save(output)
        // Bronze
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey(), 4),
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER, 3),
                HTIngredientHelper.item(ModMaterialFamilies.getMetal("tin").getBaseTagKey()),
            ).setTagCondition(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey())
            .save(output)
        // Brass
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("brass").getBaseTagKey(), 4),
                HTIngredientHelper.ingotOrDust("copper", 3),
                HTIngredientHelper.ingotOrDust("zinc"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("brass").getBaseTagKey())
            .save(output)
        // Constantan
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("copper"),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey())
            .save(output)

        // Adamant
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust("diamond"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey())
            .save(output)
        // Duratium
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("platinum"),
                HTIngredientHelper.ingotOrDust("netherite"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey())
            .save(output)
        // Energite
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("energite").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust("fluxite"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("energite").getBaseTagKey())
            .save(output)
    }

    //    Family    //

    private fun registerFamilies(family: HTMaterialFamily) {
        val tagKey: TagKey<Item> = family.getBaseTagKey()
        val item: ItemLike? = family.getBaseItem()
        if (item != null && family.entryType == HTMaterialFamily.EntryType.RAGIUM) {
            // Block <-> Base
            blockToBase(family, tagKey, item)
            // Base <-> Nugget
            nuggetToBase(family, tagKey, item)
        }

        // XXX -> Dust
        toDust(family, tagKey)
        // Raw -> Ingot
        rawToIngot(family)
    }

    private fun blockToBase(family: HTMaterialFamily, tagKey: TagKey<Item>, item: ItemLike) {
        // Block -> Base
        val blockTag: TagKey<Item> = family.getTagKey(HTMaterialVariant.STORAGE_BLOCK) ?: return
        HTShapelessRecipeBuilder(item, 9)
            .addIngredient(blockTag)
            .saveSuffixed(output, "_from_block")
        // Base -> Block
        val block: ItemLike = family.getItem(HTMaterialVariant.STORAGE_BLOCK) ?: return
        HTShapedRecipeBuilder(block)
            .hollow8()
            .define('A', tagKey)
            .define('B', item)
            .saveSuffixed(output, "_from_base")
    }

    private fun nuggetToBase(family: HTMaterialFamily, tagKey: TagKey<Item>, item: ItemLike) {
        // Base -> Nugget
        val nugget: ItemLike = family.getItem(HTMaterialVariant.NUGGET) ?: return
        HTShapelessRecipeBuilder(nugget, 9)
            .addIngredient(tagKey)
            .saveSuffixed(output, "_from_base")
        // Nugget -> Base
        val nuggetTag: TagKey<Item> = family.getTagKey(HTMaterialVariant.NUGGET) ?: return
        HTShapedRecipeBuilder(item)
            .hollow8()
            .define('A', nuggetTag)
            .define('B', nugget)
            .saveSuffixed(output, "_from_nugget")
    }

    private fun toDust(family: HTMaterialFamily, tagKey: TagKey<Item>) {
        val dust: TagKey<Item> = family.getTagKey(HTMaterialVariant.DUST) ?: return
        // Base
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(tagKey))
            .addResult(HTResultHelper.item(dust))
            .savePrefixed(output.withConditions(not(tagEmpty(dust))), "base/")
        // Gear
        // Plate
        // Rod
    }

    private fun rawToIngot(family: HTMaterialFamily) {
        val ingot: TagKey<Item> = family.getTagKey(HTMaterialVariant.INGOT) ?: return
        val raw: TagKey<Item> = family.getTagKey(HTMaterialVariant.RAW_MATERIAL) ?: return
        // Basic
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ingot, 3),
                HTIngredientHelper.item(raw, 2),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_basic_flux")
        // Advanced
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(ingot, 2),
                HTIngredientHelper.item(raw),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_advanced_flux")
    }
}
