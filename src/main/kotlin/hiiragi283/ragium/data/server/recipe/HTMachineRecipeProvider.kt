package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

object HTMachineRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerParts(output)

        RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            val validTiers: List<HTMachineTier> = entry.getOrDefault(HTMachinePropertyKeys.VALID_TIERS)
            for (tier: HTMachineTier in validTiers) {
                val nextTier: HTMachineTier = when (tier) {
                    HTMachineTier.PRIMITIVE -> HTMachineTier.SIMPLE
                    HTMachineTier.SIMPLE -> HTMachineTier.BASIC
                    HTMachineTier.BASIC -> HTMachineTier.ADVANCED
                    HTMachineTier.ADVANCED -> HTMachineTier.ELITE
                    HTMachineTier.ELITE -> null
                } ?: continue
                val currentMachine: ItemStack = key.createItemStack(tier) ?: continue
                val nextMachine: ItemStack = key.createItemStack(nextTier) ?: continue
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, nextMachine)
                    .pattern("ABA")
                    .pattern("CDC")
                    .pattern("ABA")
                    .define('A', HTTagPrefix.INGOT, nextTier.getMainMetal())
                    .define('B', nextTier.getCircuit())
                    .define('C', HTTagPrefix.INGOT, nextTier.getSteelMetal())
                    .define('D', DataComponentIngredient.of(false, currentMachine))
                    .unlockedBy("has_machine", has(currentMachine.item))
                    .save(output, RagiumAPI.id("shaped/${nextTier.serializedName}/${key.name}"))
            }
        }
    }

    private fun registerParts(output: RecipeOutput) {
        // Grate
        RagiumBlocks.Grates.entries.forEach { grate: RagiumBlocks.Grates ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, grate, 4)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', HTTagPrefix.ROD, grate.machineTier.getSteelMetal())
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_ingot", has(HTTagPrefix.INGOT, grate.machineTier.getSteelMetal()))
                .savePrefixed(output)
        }
        // Casing
        RagiumBlocks.Casings.entries.forEach { casings: RagiumBlocks.Casings ->
            val corner: Block = when (casings) {
                RagiumBlocks.Casings.SIMPLE -> Blocks.STONE
                RagiumBlocks.Casings.BASIC -> Blocks.QUARTZ_BLOCK
                RagiumBlocks.Casings.ADVANCED -> Blocks.POLISHED_DEEPSLATE
                RagiumBlocks.Casings.ELITE -> Blocks.OBSIDIAN
            }
            // Shaped Crafting
            val grate: HTBlockContent.Tier = casings.machineTier.getGrate()
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, casings, 3)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', corner)
                .define('B', grate)
                .define('C', HTTagPrefix.GEAR, casings.machineTier.getSteelMetal())
                .unlockedBy("has_grate", has(grate))
                .savePrefixed(output)
        }
        // Hull
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            // Shaped Crafting
            ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, hull, 3)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', HTTagPrefix.INGOT, hull.machineTier.getMainMetal())
                .define('B', hull.machineTier.getCircuit())
                .define('C', hull.machineTier.getCasing())
                .unlockedBy("has_casing", has(hull.machineTier.getCasing()))
                .savePrefixed(output)
        }

        // Drum
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            // Shaped Crafting
            val mainIngot: TagKey<Item> = HTTagPrefix.INGOT.createTag(drum.machineTier.getMainMetal())
            ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, drum)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, drum.machineTier.getSubMetal())
                .define('B', mainIngot)
                .define('C', Items.BUCKET)
                .unlockedBy("has_ingot", has(mainIngot))
                .savePrefixed(output)
        }
    }
}
