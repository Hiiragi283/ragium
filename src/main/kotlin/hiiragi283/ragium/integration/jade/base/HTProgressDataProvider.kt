package hiiragi283.ragium.integration.jade.base

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec2
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper

abstract class HTProgressDataProvider : HTBlockDataProvider<HTProgressData>() {
    final override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, HTProgressData> = HTProgressData.STREAM_CODEC

    final override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTProgressData,
    ) {
        val helper: IElementHelper = IElementHelper.get()
        tooltip.add(getInputElement(helper, data))
        tooltip.append(helper.spacer(4, 0))
        tooltip.append(helper.progress(data.progress / data.total.toFloat()).translate(Vec2(-2f, 0f)))
        tooltip.append(getOutputElement(helper, data))
    }

    abstract fun getInputElement(helper: IElementHelper, data: HTProgressData): IElement

    abstract fun getOutputElement(helper: IElementHelper, data: HTProgressData): IElement

    //    Item to Item    //

    abstract class ItemToItem : HTProgressDataProvider() {
        final override fun getInputElement(helper: IElementHelper, data: HTProgressData): IElement = helper.item(data.getItem(0))

        final override fun getOutputElement(helper: IElementHelper, data: HTProgressData): IElement = helper.item(data.getItem(1))
    }
}
