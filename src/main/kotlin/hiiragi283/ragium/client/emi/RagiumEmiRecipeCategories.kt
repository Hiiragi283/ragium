package hiiragi283.ragium.client.emi

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumEmiRecipeCategories {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(0, 0, 8 * 18, 3 * 18)

    @JvmStatic
    private fun create(hasText: HTHasText, id: ResourceLocation, vararg workStations: ItemLike): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(MACHINE_BOUNDS, hasText, id, *workStations)

    @JvmStatic
    private fun <T> create(recipeType: T, vararg workStations: ItemLike): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike =
        create(recipeType, recipeType.getId(), *workStations)

    //    Processor    //

    @JvmField
    val ALLOYING: HTEmiRecipeCategory = create(RagiumRecipeTypes.ALLOYING, Items.BLAST_FURNACE)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = create(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTEmiRecipeCategory = create(RagiumRecipeTypes.CUTTING, Items.STONECUTTER)

    @JvmField
    val DRYING: HTEmiRecipeCategory = create(RagiumRecipeTypes.DRYING, RagiumBlocks.DRYER)

    @JvmField
    val MELTING: HTEmiRecipeCategory = create(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTEmiRecipeCategory = create(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val PYROLYZING: HTEmiRecipeCategory = create(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    @JvmField
    val REFINING: HTEmiRecipeCategory = create(RagiumRecipeTypes.REFINING, Items.BUCKET)

    @JvmField
    val SOLIDIFYING: HTEmiRecipeCategory = create(RagiumRecipeTypes.SOLIDIFYING, HTMoldType.BLANK)
}
