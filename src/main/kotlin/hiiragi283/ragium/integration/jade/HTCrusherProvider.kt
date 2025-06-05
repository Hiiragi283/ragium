package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.integration.jade.base.HTProgressData
import hiiragi283.ragium.integration.jade.base.HTProgressDataProvider
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper
import java.util.function.Consumer

object HTCrusherProvider : HTProgressDataProvider() {
    override fun getFirstElement(helper: IElementHelper, data: HTProgressData): IElement = helper.item(data.getItem(0))

    override fun appendOutputElement(helper: IElementHelper, data: HTProgressData, consumer: Consumer<IElement>) {
        consumer.accept(helper.item(data.getItem(1)))
        consumer.accept(helper.item(data.getItem(2)))
    }

    override fun streamData(accessor: BlockAccessor): HTProgressData? {
        val crusher: HTCrusherBlockEntity = accessor.blockEntity as? HTCrusherBlockEntity ?: return null
        return HTProgressData
            .builder()
            .addItem(crusher.getItemSlot(0))
            .addItem(crusher.getItemSlot(1))
            .addItem(crusher.getItemSlot(2))
            .build(crusher.totalTick % 200, 200)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("crusher")
}
