package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.integration.jade.base.HTProgressData
import hiiragi283.ragium.integration.jade.base.HTProgressDataProvider
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper
import java.util.function.Consumer

object HTExtractorProvider : HTProgressDataProvider() {
    override fun getFirstElement(helper: IElementHelper, data: HTProgressData): IElement = helper.item(data.getItem(0))

    override fun appendOutputElement(helper: IElementHelper, data: HTProgressData, consumer: Consumer<IElement>) {
        consumer.accept(helper.item(data.getItem(1)))
    }

    override fun streamData(accessor: BlockAccessor): HTProgressData? {
        val extractor: HTExtractorBlockEntity = accessor.blockEntity as? HTExtractorBlockEntity ?: return null
        return HTProgressData
            .builder()
            .addItem(extractor.getItemSlot(0))
            .addItem(extractor.getItemSlot(1))
            .build(extractor.totalTick % 200, 200)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("extractor")
}
