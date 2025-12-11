package hiiragi283.ragium.client.integration.emi.category

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

object RagiumEmiRecipeCategories {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(0, 0, 7 * 18, 3 * 18)

    @JvmField
    val MACHINE_UPGRADE: HTEmiRecipeCategory = HTEmiRecipeCategory.create(
        MACHINE_BOUNDS,
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY),
        RagiumCommonTranslation.EMI_MACHINE_UPGRADE_TITLE::translate,
    )

    //    Generators    //

    @JvmField
    val GENERATOR_BOUNDS = HTBounds(0, 0, 7 * 18, 1 * 18)

    @JvmStatic
    private fun <ITEM> generator(item: ITEM): HTEmiRecipeCategory where ITEM : HTItemHolderLike, ITEM : HTHasText =
        HTEmiRecipeCategory.create(GENERATOR_BOUNDS, item)

    // Basic
    @JvmField
    val THERMAL: HTEmiRecipeCategory = generator(RagiumBlocks.THERMAL_GENERATOR)

    // Advanced
    @JvmField
    val CULINARY: HTEmiRecipeCategory = generator(RagiumBlocks.CULINARY_GENERATOR)

    @JvmField
    val MAGMATIC: HTEmiRecipeCategory = generator(RagiumBlocks.MAGMATIC_GENERATOR)

    // Elite
    @JvmField
    val COOLANT: HTEmiRecipeCategory = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    //    Processors    //

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg workStations: ItemLike,
    ): HTEmiRecipeCategory = HTEmiRecipeCategory.create(MACHINE_BOUNDS, recipeType, *workStations)

    // Basic
    @JvmField
    val ALLOYING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.CRUSHING, RagiumBlocks.PULVERIZER, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val EXTRACTING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    // Advanced
    @JvmField
    val MELTING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, RagiumBlocks.ADVANCED_MIXER)

    @JvmField
    val REFINING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY)

    @JvmField
    val SOLIDIFYING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.SOLIDIFYING, RagiumBlocks.REFINERY)

    // Elite
    @JvmField
    val BREWING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.BREWING, RagiumBlocks.BREWERY)

    @JvmField
    val PLANTING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    // Ultimate
    @JvmField
    val ENCHANTING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val SIMULATING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.SIMULATING, RagiumBlocks.SIMULATOR)

    //    Device    //

    @JvmField
    val ROCK_GENERATING: HTEmiRecipeCategory =
        machine(RagiumRecipeTypes.ROCK_GENERATING, RagiumBlocks.STONE_COLLECTOR)

    //    Register    //

    @JvmStatic
    fun register(registry: EmiRegistry) {
        register(registry, MACHINE_UPGRADE)
        // Generator
        register(registry, THERMAL)

        register(registry, CULINARY)
        register(registry, MAGMATIC)

        register(registry, COOLANT)
        register(registry, COMBUSTION)
        // Processor
        register(registry, ALLOYING)
        register(registry, COMPRESSING)
        register(registry, CRUSHING)
        register(registry, CUTTING)
        register(registry, EXTRACTING)

        register(registry, MELTING)
        register(registry, MIXING)
        register(registry, REFINING)
        register(registry, SOLIDIFYING)

        register(registry, BREWING)
        register(registry, PLANTING)
        for (block: ItemLike in listOf(RagiumBlocks.ELECTRIC_FURNACE, RagiumBlocks.MULTI_SMELTER)) {
            registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMOKING, block.toEmi())
        }

        register(registry, ENCHANTING)
        register(registry, SIMULATING)
        // Device
        register(registry, ROCK_GENERATING)
    }

    @JvmStatic
    private fun register(registry: EmiRegistry, category: HTEmiRecipeCategory) {
        registry.addCategory(category)
        category.workStations.forEach(registry::addWorkstation.partially1(category))
    }
}
