package hiiragi283.ragium.data.client

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.util.emptyOptional
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.widget.HTEnergySlotWidgetRenderer
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile
import net.neoforged.neoforge.common.data.SpriteSourceProvider

class RagiumSpriteSourceProvider(context: HTDataGenContext) :
    SpriteSourceProvider(context.output, context.registries, RagiumAPI.MOD_ID, context.fileHelper) {
    override fun gather() {
        atlas(BLOCKS_ATLAS).addSource(SingleFile(HTEnergySlotWidgetRenderer.SPRITE, emptyOptional()))
    }
}
