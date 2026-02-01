package hiiragi283.ragium.client.emi

import dev.emi.emi.api.widget.Bounds
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.toText
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumEmiRecipeCategories {
    //    Generator    //

    @JvmField
    val GENERATOR_BOUNDS = Bounds(0, 0, 7 * 18, 1 * 18)

    @JvmStatic
    private fun generator(item: HTItemHolderLike<*>): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(GENERATOR_BOUNDS, item, item.getId(), item)

    @JvmField
    val THERMAL: HTEmiRecipeCategory = generator(HTItemHolderLike.of(Items.COAL))

    @JvmField
    val CULINARY: HTEmiRecipeCategory = generator(HTItemHolderLike.of(Items.GOLDEN_APPLE))

    @JvmField
    val MAGMATIC: HTEmiRecipeCategory = generator(HTItemHolderLike.of(Items.LAVA_BUCKET))

    @JvmField
    val COOLANT: HTEmiRecipeCategory = generator(HTItemHolderLike.of(Items.ICE))

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(HTItemHolderLike.of(Items.BLAZE_POWDER))

    //    Processor    //

    @JvmField
    val PROCESSOR_BOUNDS = Bounds(0, 0, 8 * 18, 3 * 18)

    @JvmStatic
    private fun processor(hasText: HTHasText, id: ResourceLocation, vararg workStations: ItemLike): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(PROCESSOR_BOUNDS, hasText, id, *workStations)

    @JvmStatic
    private fun <T> processor(recipeType: T, vararg workStations: ItemLike): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike =
        processor(recipeType, recipeType.getId(), *workStations)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val PRESSING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS)

    // Machine - Heat
    @JvmField
    val MELTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    @JvmField
    val REFINING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.REFINING, Items.BUCKET)

    @JvmField
    val SOLIDIFYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.SOLIDIFYING, RagiumBlocks.SOLIDIFIER)

    // Machine - Chemical
    @JvmField
    val MIXING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val WASHING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.WASHING, RagiumBlocks.MIXER)

    // val REACTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.REACTING, RagiumBlocks.MIXER)

    // Machine - Matter

    // Device - Basic

    @JvmField
    val FERMENTING: HTEmiRecipeCategory = processor("Fermenting"::toText, RagiumAPI.id("fermenting"), RagiumBlocks.FERMENTER)

    @JvmField
    val PLANTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    // Device - Advanced

    // Device - Enchanting
    @JvmField
    val ENCHANTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)
}
