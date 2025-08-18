package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.data.server.material.ModMaterialFamilies
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
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
            .define('A', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTMaterialVariant.INGOT, HTVanillaMaterialType.COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                HTIngredientHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 2),
            ).save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTMaterialVariant.FUEL, HTVanillaMaterialType.COAL)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.getCompound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                HTIngredientHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4),
            ).save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL))
            .hollow8()
            .define('A', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('B', HTMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
                HTIngredientHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 6),
            ).save(output)
        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.getGem(RagiumMaterialType.AZURE), 2)
            .mosaic4()
            .define('A', Tags.Items.GEMS_AMETHYST)
            .define('B', Tags.Items.GEMS_LAPIS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.getCompound(RagiumMaterialType.AZURE_STEEL))
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.AZURE)
            .define('B', HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL))
            .addIngredient(RagiumItems.getCompound(RagiumMaterialType.AZURE_STEEL))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.AZURE, 2),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.AMETHYST),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.LAPIS),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE, 2),
            ).save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.SILICON)
            .addIngredient(gemOrDust(RagiumMaterialType.AZURE))
            .setExp(0.7f)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SILICON, 2),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.SILICON, 4),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.AZURE),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).saveSuffixed(output, "_alt")
        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', HTMaterialVariant.DUST, RagiumMaterialType.WOOD)
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
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .define('B', HTMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 9),
                HTIngredientHelper.item(HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL),
                HTIngredientHelper.item(HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL),
                HTIngredientHelper.item(RagiumModTags.Items.ELDRITCH_PEARL_BINDER),
            ).save(output)

        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL),
                HTIngredientHelper.fluid(RagiumFluidContents.WARPED_SAP, 1000),
                HTResultHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).save(output)

        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 1000),
                HTResultHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).saveSuffixed(output, "_alt")

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                HTIngredientHelper.item(Tags.Items.ENDER_PEARLS),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
                HTResultHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL),
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
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(output)

        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_DEEP_SCRAP))
            .addResult(HTResultHelper.item(RagiumItems.DEEP_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
        // Iridescentium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.IRIDESCENTIUM),
                HTIngredientHelper.gemOrDust(RagiumMaterialType.ELDRITCH_PEARL, 8),
                HTIngredientHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
        // Other
        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(HTMaterialVariant.DUST, RagiumMaterialType.SULFUR)
            .addIngredient(HTMaterialVariant.DUST, RagiumMaterialType.SALTPETER)
            .addIngredient(fuelOrDust(HTVanillaMaterialType.CHARCOAL))
            .addIngredient(RagiumModTags.Items.TOOLS_HAMMER)
            .saveSuffixed(output, "_with_hammer")
    }

    private fun oreToRaw() {
        // Coal
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, HTVanillaMaterialType.COAL))
            .addResult(HTResultHelper.item(HTMaterialVariant.FUEL, HTVanillaMaterialType.COAL, 2))
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.SULFUR), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Copper
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, HTVanillaMaterialType.COPPER))
            .addResult(HTResultHelper.item(HTMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER, 4))
            .addResult(HTResultHelper.item(HTMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, HTVanillaMaterialType.IRON))
            .addResult(HTResultHelper.item(HTMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.IRON, 2))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, HTVanillaMaterialType.GOLD))
            .addResult(HTResultHelper.item(HTMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.GOLD, 2))
            .addResult(HTResultHelper.item(HTMaterialVariant.RAW_MATERIAL, HTVanillaMaterialType.COPPER), 1 / 4f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, HTVanillaMaterialType.REDSTONE))
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 8))
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 4), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Raginite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, RagiumMaterialType.RAGINITE))
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 8))
            .addResult(HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL, 1), 1 / 4f)
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
                .crushing(HTIngredientHelper.item(HTMaterialVariant.ORE, material))
                .addResult(HTResultHelper.item(HTMaterialVariant.GEM, material, count))
                .saveSuffixed(output, "_from_ore")
        }

        // Netherite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_NETHERITE_SCRAP))
            .addResult(HTResultHelper.item(Items.NETHERITE_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    @Suppress("DEPRECATION")
    private fun alloying() {
        // Netherite
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(Tags.Items.INGOTS_NETHERITE, 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 4),
                HTIngredientHelper.item(Items.NETHERITE_SCRAP, 4),
            ).save(output)
        // Deep Steel
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL, 2),
                HTIngredientHelper.ingotOrDust(RagiumMaterialType.AZURE_STEEL, 4),
                HTIngredientHelper.item(RagiumItems.DEEP_SCRAP, 4),
            ).save(output)

        // Steel
        val steelTag: TagKey<Item> = ModMaterialFamilies.getAlloy("steel").getBaseTagKey()
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(steelTag),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL, 2),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(steelTag),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON),
                HTIngredientHelper.fuelOrDust(RagiumMaterialType.COAL_COKE),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("invar").getBaseTagKey(), 3),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.IRON, 2),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("invar").getBaseTagKey())
            .save(output)
        // Electrum
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.GOLD),
                HTIngredientHelper.ingotOrDust("silver"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey())
            .save(output)
        // Bronze
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey(), 4),
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER, 3),
                HTIngredientHelper.ingotOrDust("tin"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey())
            .save(output)
        // Brass
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("brass").getBaseTagKey(), 4),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 3),
                HTIngredientHelper.ingotOrDust("zinc"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("brass").getBaseTagKey())
            .save(output)
        // Constantan
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.COPPER),
                HTIngredientHelper.ingotOrDust("nickel"),
            ).setTagCondition(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey())
            .save(output)

        // Adamant
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust(HTVanillaMaterialType.DIAMOND),
            ).setTagCondition(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey())
            .save(output)
        // Duratium
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey(), 2),
                HTIngredientHelper.ingotOrDust("platinum"),
                HTIngredientHelper.ingotOrDust(HTVanillaMaterialType.NETHERITE),
            ).setTagCondition(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey())
            .save(output)
        // Energite
        HTCombineItemToObjRecipeBuilder
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
        HTItemToObjRecipeBuilder
            .pulverizing(HTIngredientHelper.item(tagKey), HTResultHelper.item(dust))
            .savePrefixed(output.withConditions(not(tagEmpty(dust))), "base/")
        // Gear
        // Plate
        // Rod
    }

    private fun rawToIngot(family: HTMaterialFamily) {
        val ingot: TagKey<Item> = family.getTagKey(HTMaterialVariant.INGOT) ?: return
        val raw: TagKey<Item> = family.getTagKey(HTMaterialVariant.RAW_MATERIAL) ?: return
        // Basic
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ingot, 3),
                HTIngredientHelper.item(raw, 2),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_basic_flux")
        // Advanced
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(ingot, 2),
                HTIngredientHelper.item(raw),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_advanced_flux")
    }
}
