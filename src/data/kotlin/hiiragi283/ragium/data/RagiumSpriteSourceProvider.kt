package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.widget.HTEnergySlotWidgetRenderer
import java.util.Optional
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SpriteSourceProvider
import java.util.concurrent.CompletableFuture

class RagiumSpriteSourceProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : SpriteSourceProvider(output, lookupProvider, RagiumAPI.MOD_ID, fileHelper) {
    override fun gather() {
        atlas(BLOCKS_ATLAS).addSource(SingleFile(HTEnergySlotWidgetRenderer.SPRITE, Optional.empty()))
    }
}
