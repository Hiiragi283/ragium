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
    val THERMAL: HTEmiRecipeCategory = generator(HTItemHolderLike.Simple(Items.COAL))

    @JvmField
    val CULINARY: HTEmiRecipeCategory = generator(HTItemHolderLike.Simple(Items.GOLDEN_APPLE))

    @JvmField
    val MAGMATIC: HTEmiRecipeCategory = generator(HTItemHolderLike.Simple(Items.LAVA_BUCKET))

    @JvmField
    val COOLANT: HTEmiRecipeCategory = generator(HTItemHolderLike.Simple(Items.ICE))

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(HTItemHolderLike.Simple(Items.BLAZE_POWDER))

    //    Processor    //

    @JvmStatic
    private fun processor(hasText: HTHasText, id: ResourceLocation, vararg workStations: ItemLike): HTEmiRecipeCategory =
        processor(hasText, id, 18 * 8, 18 * 2, *workStations)

    @JvmStatic
    private fun <T> processor(recipeType: T, vararg workStations: ItemLike): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike =
        processor(recipeType, recipeType.getId(), *workStations)

    @JvmStatic
    private fun processor(
        hasText: HTHasText,
        id: ResourceLocation,
        width: Int,
        height: Int,
        vararg workStations: ItemLike,
    ): HTEmiRecipeCategory = HTEmiRecipeCategory.create(Bounds(0, 0, width, height), hasText, id, *workStations)

    @JvmStatic
    private fun <T> processor(
        recipeType: T,
        width: Int,
        height: Int,
        vararg workStations: ItemLike,
    ): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike = processor(recipeType, recipeType.getId(), width, height, *workStations)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val PRESSING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS)

    // Machine - Advanced
    @JvmField
    val DRYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.DRYING, RagiumBlocks.DRYER)

    @JvmField
    val MELTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MIXING, 18 * 8, 18 * 3, RagiumBlocks.MIXER)

    @JvmField
    val PYROLYZING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    @JvmField
    val REFINING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.REFINING, Items.BUCKET)

    @JvmField
    val SIMULATING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.SIMULATING, Items.DRAGON_EGG)

    // Machine - Extra
    @JvmField
    val SOLIDIFYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.SOLIDIFYING, RagiumBlocks.SOLIDIFIER)

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
