package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.common.Tags

object RagiumPressingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        createPressing()
            .itemOutput(RagiumCommonTags.Items.PLATES_PLASTIC)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .save(output)
        // Synthetic Fiber
        createPressing()
            .itemOutput(RagiumItems.SYNTHETIC_FIBER, 2)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .catalyst(Tags.Items.STRINGS)
            .save(output)
        // Synthetic Leather
        createPressing()
            .itemOutput(RagiumItems.SYNTHETIC_LEATHER, 2)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .catalyst(Tags.Items.LEATHERS)
            .save(output)
    }
}
