package hiiragi283.ragium.client.emi

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumEmiRecipeCategories {
    //    Generator    //

    @JvmField
    val GENERATOR_BOUNDS = HTBounds(0, 0, 7 * 18, 1 * 18)

    @JvmStatic
    private fun generator(item: HTItemHolderLike<*>): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(GENERATOR_BOUNDS, item, item.getId(), item)

    @JvmField
    val THERMAL: HTEmiRecipeCategory = generator(Items.COAL.toHolderLike())

    @JvmField
    val CULINARY: HTEmiRecipeCategory = generator(Items.GOLDEN_APPLE.toHolderLike())

    @JvmField
    val MAGMATIC: HTEmiRecipeCategory = generator(Items.LAVA_BUCKET.toHolderLike())

    @JvmField
    val COOLANT: HTEmiRecipeCategory = generator(Items.ICE.toHolderLike())

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(Items.BLAZE_POWDER.toHolderLike())

    //    Processor    //

    @JvmField
    val PROCESSOR_BOUNDS = HTBounds(0, 0, 8 * 18, 3 * 18)

    @JvmStatic
    private fun processor(hasText: HTHasText, id: ResourceLocation, vararg workStations: ItemLike): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(PROCESSOR_BOUNDS, hasText, id, *workStations)

    @JvmStatic
    private fun <T> processor(recipeType: T, vararg workStations: ItemLike): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike =
        processor(recipeType, recipeType.getId(), *workStations)

    @JvmField
    val ALLOYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val DRYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.DRYING, RagiumBlocks.DRYER)

    @JvmField
    val MELTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val PLANTING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    @JvmField
    val PRESSING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PRESSING, Items.PISTON)

    @JvmField
    val PYROLYZING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    @JvmField
    val REFINING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.REFINING, Items.BUCKET)

    @JvmField
    val SOLIDIFYING: HTEmiRecipeCategory = processor(RagiumRecipeTypes.SOLIDIFYING, HTMoldType.BLANK)
}
