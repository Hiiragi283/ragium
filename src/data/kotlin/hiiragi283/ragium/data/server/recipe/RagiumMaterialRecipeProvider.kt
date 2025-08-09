package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.data.server.material.ModMaterialFamilies
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        HTMaterialFamily.instances.values.forEach(::registerFamilies)

        oreToRaw()
        alloying()
    }

    // Ore -> Raw/Gem
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
            .addResult(HTResultHelper.item(Items.RAW_COPPER, 4))
            .addResult(HTResultHelper.item(Items.RAW_GOLD), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_IRON))
            .addResult(HTResultHelper.item(Items.RAW_IRON, 2))
            .addResult(HTResultHelper.item(Items.FLINT), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_GOLD))
            .addResult(HTResultHelper.item(Items.RAW_GOLD, 2))
            .addResult(HTResultHelper.item(Items.RAW_COPPER), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_REDSTONE))
            .addResult(HTResultHelper.item(Items.REDSTONE, 8))
            .addResult(HTResultHelper.item(Items.REDSTONE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_CINNABAR, 4), 1 / 2f)
            .saveSuffixed(output, "_from_ore")
        // Lapis
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_LAPIS))
            .addResult(HTResultHelper.item(Items.LAPIS_LAZULI, 8))
            .saveSuffixed(output, "_from_ore")
        // Quartz
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_QUARTZ))
            .addResult(HTResultHelper.item(Items.QUARTZ, 4))
            .saveSuffixed(output, "_from_ore")
        // Diamond
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_DIAMOND))
            .addResult(HTResultHelper.item(Items.DIAMOND, 2))
            .saveSuffixed(output, "_from_ore")
        // Emerald
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_EMERALD))
            .addResult(HTResultHelper.item(Items.EMERALD, 2))
            .saveSuffixed(output, "_from_ore")
        // Netherite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(Tags.Items.ORES_NETHERITE_SCRAP))
            .addResult(HTResultHelper.item(Items.NETHERITE_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")

        // Raginite
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_RAGINITE))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 8))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.DUSTS_RAGINITE, 4), 1 / 2f)
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2), 1 / 4f)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_RAGI_CRYSTAL))
            .addResult(HTResultHelper.item(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2))
            .saveSuffixed(output, "_from_ore")
        // Deep Steel
        HTItemToChancedItemRecipeBuilder
            .crushing(HTIngredientHelper.item(RagiumCommonTags.Items.ORES_DEEP_SCRAP))
            .addResult(HTResultHelper.item(RagiumItems.DEEP_SCRAP, 2))
            .saveSuffixed(output, "_from_ore")
    }

    private fun alloying() {
        // Steel
        val steelTag: TagKey<Item> = ModMaterialFamilies.getAlloy("steel").getBaseTagKey()
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("iron"),
                HTIngredientHelper.item(HTIngredientHelper.coal(), 2),
                HTResultHelper.item(steelTag),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coal")

        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("iron"),
                HTIngredientHelper.item(HTIngredientHelper.coalCoke()),
                HTResultHelper.item(steelTag),
            ).setTagCondition(steelTag)
            .saveSuffixed(output, "_from_coke")
        // Invar
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("iron", 2),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("invar").getBaseTagKey(), 3),
            ).setTagCondition(ModMaterialFamilies.getAlloy("invar").getBaseTagKey())
            .save(output)
        // Electrum
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("gold"),
                HTIngredientHelper.ingotOrDust("silver"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey(), 2),
            ).setTagCondition(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey())
            .save(output)
        // Bronze
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.item(Tags.Items.INGOTS_COPPER, 3),
                HTIngredientHelper.item(ModMaterialFamilies.getMetal("tin").getBaseTagKey()),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey(), 4),
            ).setTagCondition(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey())
            .save(output)
        // Brass
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("copper", 3),
                HTIngredientHelper.ingotOrDust("zinc"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("brass").getBaseTagKey(), 4),
            ).setTagCondition(ModMaterialFamilies.getAlloy("brass").getBaseTagKey())
            .save(output)
        // Constantan
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("copper"),
                HTIngredientHelper.ingotOrDust("nickel"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey(), 2),
            ).setTagCondition(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey())
            .save(output)

        // Adamant
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust("diamond"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey(), 2),
            ).setTagCondition(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey())
            .save(output)
        // Duratium
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("platinum"),
                HTIngredientHelper.ingotOrDust("netherite"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey(), 2),
            ).setTagCondition(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey())
            .save(output)
        // Energite
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.ingotOrDust("nickel"),
                HTIngredientHelper.gemOrDust("fluxite"),
                HTResultHelper.item(ModMaterialFamilies.getAlloy("energite").getBaseTagKey(), 2),
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
        val blockTag: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.STORAGE_BLOCKS) ?: return
        HTShapelessRecipeBuilder(item, 9)
            .addIngredient(blockTag)
            .saveSuffixed(output, "_from_block")
        // Base -> Block
        val block: ItemLike = family.getItem(HTMaterialFamily.Variant.STORAGE_BLOCKS) ?: return
        HTShapedRecipeBuilder(block)
            .hollow8()
            .define('A', tagKey)
            .define('B', item)
            .saveSuffixed(output, "_from_base")
    }

    private fun nuggetToBase(family: HTMaterialFamily, tagKey: TagKey<Item>, item: ItemLike) {
        // Base -> Nugget
        val nugget: ItemLike = family.getItem(HTMaterialFamily.Variant.NUGGETS) ?: return
        HTShapelessRecipeBuilder(nugget, 9)
            .addIngredient(tagKey)
            .saveSuffixed(output, "_from_base")
        // Nugget -> Base
        val nuggetTag: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.NUGGETS) ?: return
        HTShapedRecipeBuilder(item)
            .hollow8()
            .define('A', nuggetTag)
            .define('B', nugget)
            .saveSuffixed(output, "_from_nugget")
    }

    private fun toDust(family: HTMaterialFamily, tagKey: TagKey<Item>) {
        val dust: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.DUSTS) ?: return
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
        val ingot: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.INGOTS) ?: return
        val raw: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.RAW_MATERIALS) ?: return
        // Basic
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.item(raw, 2),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
                HTResultHelper.item(ingot, 3),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_basic_flux")
        // Advanced
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTIngredientHelper.item(raw),
                HTIngredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
                HTResultHelper.item(ingot, 2),
            ).setTagCondition(ingot)
            .saveSuffixed(output, "_with_advanced_flux")
    }
}
