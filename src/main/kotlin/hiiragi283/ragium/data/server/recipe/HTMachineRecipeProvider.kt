package hiiragi283.ragium.data.server.recipe

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.firstTier
import hiiragi283.ragium.api.extension.validTiers
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import org.slf4j.Logger

object HTMachineRecipeProvider : RecipeProviderChild {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    override fun buildRecipes(output: RecipeOutput) {
        registerParts(output)

        registerMachines(output)

        registerMachineUpdates(output)
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
            val corner: Item = when (casings) {
                RagiumBlocks.Casings.BASIC -> Items.STONE
                RagiumBlocks.Casings.ADVANCED -> Items.QUARTZ_BLOCK
                RagiumBlocks.Casings.ELITE -> Items.POLISHED_DEEPSLATE
                RagiumBlocks.Casings.ULTIMATE -> Items.OBSIDIAN
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
                .define('B', hull.machineTier.getCircuitTag())
                .define('C', hull.machineTier.getCasing())
                .unlockedBy("has_casing", has(hull.machineTier.getCasing()))
                .savePrefixed(output)
        }
        // Coil
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            val previousTier: HTMachineTier = coil.machineTier.getPreviousTier() ?: HTMachineTier.BASIC
            // Assembler
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.ASSEMBLER, previousTier)
                .itemInput(HTTagPrefix.INGOT, coil.machineTier.getSubMetal(), 8)
                .itemInput(RagiumBlocks.SHAFT)
                .itemOutput(coil, 2)
                .save(output)
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

    private fun registerMachines(output: RecipeOutput) {
        // Manual Machine
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.MANUAL_GRINDER)
            .pattern("A  ")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.RODS_WOODEN)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('C', Items.BRICKS)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)

        mapOf(
            // consumer
            // generator
            // processor
            RagiumMachineKeys.ASSEMBLER to Items.CRAFTER,
            RagiumMachineKeys.BLAST_FURNACE to Items.BLAST_FURNACE,
            RagiumMachineKeys.CHEMICAL_REACTOR to Items.GLASS,
            RagiumMachineKeys.COMPRESSOR to Items.PISTON,
            RagiumMachineKeys.CUTTING_MACHINE to Items.STONECUTTER,
            // RagiumMachineKeys.DISTILLATION_TOWER to Items.CRAFTER,
            RagiumMachineKeys.EXTRACTOR to Items.HOPPER,
            RagiumMachineKeys.GRINDER to Items.GRINDSTONE,
            RagiumMachineKeys.GROWTH_CHAMBER to Items.IRON_HOE,
            RagiumMachineKeys.LASER_TRANSFORMER to RagiumItems.LASER_EMITTER,
            RagiumMachineKeys.MIXER to Items.CAULDRON,
            RagiumMachineKeys.MULTI_SMELTER to Items.FURNACE,
        ).forEach { (key: HTMachineKey, input: ItemLike) ->
            val entry: HTMachineRegistry.Entry = key.getEntryOrNull() ?: return@forEach
            runCatching {
                val firstTier: HTMachineTier = entry.firstTier
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, entry.createItemStack(firstTier)!!)
                    .pattern("ABA")
                    .pattern("CDC")
                    .pattern("ABA")
                    .define('A', HTTagPrefix.INGOT, firstTier.getMainMetal())
                    .define('B', firstTier.getCircuitTag())
                    .define('C', input)
                    .define('D', firstTier.getCasing())
                    .unlockedBy("has_casing", has(firstTier.getCasing()))
                    .save(output, RagiumAPI.id("shaped/${key.name}"))
            }.onFailure { throwable: Throwable -> LOGGER.error(throwable.localizedMessage) }
        }
    }

    private fun registerMachineUpdates(output: RecipeOutput) {
        RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            for (tier: HTMachineTier in entry.validTiers) {
                val nextTier: HTMachineTier = tier.getNextTier() ?: continue
                val currentMachine: ItemStack = key.createItemStack(tier) ?: continue
                val nextMachine: ItemStack = key.createItemStack(nextTier) ?: continue
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, nextMachine)
                    .pattern("ABA")
                    .pattern("CDC")
                    .pattern("ABA")
                    .define('A', HTTagPrefix.INGOT, nextTier.getMainMetal())
                    .define('B', nextTier.getCircuitTag())
                    .define('C', HTTagPrefix.INGOT, nextTier.getSteelMetal())
                    .define(
                        'D',
                        DataComponentIngredient.of(
                            false,
                            RagiumComponentTypes.MACHINE_TIER.get(),
                            tier,
                            currentMachine.item,
                        ),
                    ).unlockedBy("has_machine", has(currentMachine.item))
                    .save(output, RagiumAPI.id("shaped/${nextTier.serializedName}/${key.name}"))
            }
        }
    }
}
