package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
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
        createCrushing()
            .itemOutput(Items.COAL, 2)
            .itemOutput(RagiumItems.SULFUR_DUST, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_COAL)
            .saveSuffixed(output, "_from_ore")
        // Copper
        createCrushing()
            .itemOutput(Items.RAW_COPPER, 4)
            .itemOutput(Items.RAW_GOLD, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_COPPER)
            .saveSuffixed(output, "_from_ore")
        // Iron
        createCrushing()
            .itemOutput(Items.RAW_IRON, 2)
            .itemOutput(Items.FLINT, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_IRON)
            .saveSuffixed(output, "_from_ore")
        // Gold
        createCrushing()
            .itemOutput(Items.RAW_GOLD, 2)
            .itemOutput(Items.RAW_COPPER, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_GOLD)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        createCrushing()
            .itemOutput(Items.REDSTONE, 8)
            .itemOutput(Items.REDSTONE, 4, 1 / 2f)
            .itemOutput(RagiumCommonTags.Items.DUSTS_CINNABAR, 2, 1 / 2f)
            .itemInput(Tags.Items.ORES_REDSTONE)
            .saveSuffixed(output, "_from_ore")
        // Lapis
        createCrushing()
            .itemOutput(Items.LAPIS_LAZULI, 8)
            .itemInput(Tags.Items.ORES_LAPIS)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        createCrushing()
            .itemOutput(Items.QUARTZ, 4)
            .itemInput(Tags.Items.ORES_QUARTZ)
            .saveSuffixed(output, "_from_ore")
        // Diamond
        createCrushing()
            .itemOutput(Items.DIAMOND, 2)
            .itemInput(Tags.Items.ORES_DIAMOND)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        createCrushing()
            .itemOutput(Items.EMERALD, 2)
            .itemInput(Tags.Items.ORES_EMERALD)
            .saveSuffixed(output, "_from_ore")
        // Netherite
        createCrushing()
            .itemOutput(Items.NETHERITE_SCRAP, 2)
            .itemInput(Tags.Items.ORES_NETHERITE_SCRAP)
            .saveSuffixed(output, "_from_ore")

        // Raginite
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.DUSTS_RAGINITE, 8)
            .itemOutput(RagiumCommonTags.Items.DUSTS_RAGINITE, 4, 1 / 2f)
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2, 1 / 4f)
            .itemInput(RagiumCommonTags.Items.ORES_RAGINITE)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2)
            .itemInput(RagiumCommonTags.Items.ORES_RAGI_CRYSTAL)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel
        createCrushing()
            .itemOutput(RagiumItems.DEEP_SCRAP, 2)
            .itemInput(RagiumCommonTags.Items.ORES_DEEP_SCRAP)
            .saveSuffixed(output, "_from_ore")
    }

    private fun alloying() {
        // Steel
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("steel").getBaseTagKey(), appendCondition = true)
            .itemInput(Tags.Items.INGOTS_IRON)
            .itemInput(Items.COAL, 2)
            .saveSuffixed(output, "_from_coal")

        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("steel").getBaseTagKey(), appendCondition = true)
            .itemInput(Tags.Items.INGOTS_IRON)
            .itemInput(RagiumCommonTags.Items.COAL_COKES)
            .saveSuffixed(output, "_from_coke")
        // Invar
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("invar").getBaseTagKey(), 3, appendCondition = true)
            .itemInput(Tags.Items.INGOTS_IRON, 2)
            .itemInput(ModMaterialFamilies.getMetal("nickel").getBaseTagKey())
            .save(output)
        // Electrum
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("electrum").getBaseTagKey(), 2, appendCondition = true)
            .itemInput(Tags.Items.INGOTS_GOLD)
            .itemInput(ModMaterialFamilies.getMetal("silver").getBaseTagKey())
            .save(output)
        // Bronze
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("bronze").getBaseTagKey(), 4, appendCondition = true)
            .itemInput(Tags.Items.INGOTS_COPPER, 3)
            .itemInput(ModMaterialFamilies.getMetal("tin").getBaseTagKey())
            .save(output)
        // Brass
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("brass").getBaseTagKey(), 4, appendCondition = true)
            .itemInput(Tags.Items.INGOTS_COPPER, 3)
            .itemInput(ModMaterialFamilies.getMetal("zinc").getBaseTagKey())
            .save(output)
        // Constantan
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("constantan").getBaseTagKey(), 2, appendCondition = true)
            .itemInput(Tags.Items.INGOTS_COPPER)
            .itemInput(ModMaterialFamilies.getMetal("nickel").getBaseTagKey())
            .save(output)

        // Adamant
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("adamant").getBaseTagKey(), appendCondition = true)
            .itemInput(ModMaterialFamilies.getMetal("nickel").getBaseTagKey())
            .itemInput(Tags.Items.GEMS_DIAMOND)
            .save(output)
        // Duratium
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("duratium").getBaseTagKey(), appendCondition = true)
            .itemInput(ModMaterialFamilies.getMetal("platinum").getBaseTagKey())
            .itemInput(Tags.Items.INGOTS_NETHERITE)
            .save(output)
        // Energite
        createAlloying()
            .itemOutput(ModMaterialFamilies.getAlloy("energite").getBaseTagKey(), appendCondition = true)
            .itemInput(ModMaterialFamilies.getMetal("nickel").getBaseTagKey())
            .itemInput(ModMaterialFamilies.getGem("fluxite").getBaseTagKey())
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
        createCrushing()
            .itemOutput(dust, appendCondition = family.getItem(HTMaterialFamily.Variant.DUSTS) == null)
            .itemInput(tagKey)
            .savePrefixed(output, "base/")
        // Gear
        // Plate
        // Rod
    }

    private fun rawToIngot(family: HTMaterialFamily) {
        val ingot: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.INGOTS) ?: return
        val raw: TagKey<Item> = family.getTagKey(HTMaterialFamily.Variant.RAW_MATERIALS) ?: return
        // Basic
        createAlloying()
            .itemOutput(ingot, 3, appendCondition = family.entryType.isMod)
            .itemInput(raw, 2)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .saveSuffixed(output, "_with_basic_flux")
        // Advanced
        createAlloying()
            .itemOutput(ingot, 2, appendCondition = family.entryType.isMod)
            .itemInput(raw)
            .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .saveSuffixed(output, "_with_advanced_flux")
    }
}
