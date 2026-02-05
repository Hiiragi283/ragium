package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPressingRecipeBuilder
import net.minecraft.world.item.Items

object RagiumPressingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // XX Banner
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val banner: HTItemHolderLike<*> = HTItemHolderLike.of(HTConst.MINECRAFT.toId("${color.serializedName}_banner"))
            HTPressingRecipeBuilder.printing(output) {
                top = inputCreator.create(banner)
                bottom = inputCreator.create(banner)
                result = resultCreator.create(banner)
            }
        }

        // Book -> Written Book
        HTPressingRecipeBuilder.printing(output) {
            top = inputCreator.create(Items.BOOK)
            bottom = inputCreator.create(Items.WRITTEN_BOOK)
            result = resultCreator.create(Items.WRITTEN_BOOK)
        }
        // Map -> Filled Map
        HTPressingRecipeBuilder.printing(output) {
            top = inputCreator.create(Items.MAP)
            bottom = inputCreator.create(Items.FILLED_MAP)
            result = resultCreator.create(Items.FILLED_MAP)
        }
    }
}
