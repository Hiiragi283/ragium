package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags

object RagiumPressingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        createPressing()
            .itemOutput(RagiumCommonTags.Items.PLATES_PLASTIC)
            .itemInput(RagiumModTags.Items.POLYMER_RESIN)
            .save(output)
    }
}
