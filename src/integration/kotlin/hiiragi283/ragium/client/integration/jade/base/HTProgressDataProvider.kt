package hiiragi283.ragium.client.integration.jade.base

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec2
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper
import java.util.function.Consumer

abstract class HTProgressDataProvider : HTBlockDataProvider<HTProgressData>() {
    final override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, HTProgressData> = TODO()

    final override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTProgressData,
    ) {
        val helper: IElementHelper = IElementHelper.get()
        tooltip.add(getFirstElement(helper, data))
        appendInputElement(helper, data, tooltip::append)
        tooltip.append(helper.spacer(4, 0))
        tooltip.append(helper.progress(data.progress / data.total.toFloat()).translate(Vec2(-2f, 0f)))
        appendOutputElement(helper, data, tooltip::append)
    }

    abstract fun getFirstElement(helper: IElementHelper, data: HTProgressData): IElement

    open fun appendInputElement(helper: IElementHelper, data: HTProgressData, consumer: Consumer<IElement>) {}

    open fun appendOutputElement(helper: IElementHelper, data: HTProgressData, consumer: Consumer<IElement>) {}
}
