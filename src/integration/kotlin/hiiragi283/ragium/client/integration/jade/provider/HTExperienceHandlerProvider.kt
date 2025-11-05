package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.integration.jade.HTBasicJadeDataProvider
import hiiragi283.ragium.api.storage.capability.HTExperienceCapabilities
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.longText
import net.minecraft.ChatFormatting
import snownee.jade.api.Accessor
import snownee.jade.api.BlockAccessor
import snownee.jade.api.EntityAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

sealed class HTExperienceHandlerProvider<ACCESSOR : Accessor<*>> :
    HTBasicJadeDataProvider<ACCESSOR, List<HTExperienceView>>(
        "experience_handler",
        HTExperienceView.CODEC.listOf().cast(),
    ) {
    final override fun appendTooltip(
        tooltip: ITooltip,
        accessor: ACCESSOR,
        config: IPluginConfig,
        data: List<HTExperienceView>,
    ) {
        for (view: HTExperienceView in data) {
            tooltip.add(RagiumTranslation.JADE_EXP_STORAGE.getComponent(longText(view.getAmount()).withStyle(ChatFormatting.WHITE)))
        }
    }

    data object ForBlocks : HTExperienceHandlerProvider<BlockAccessor>() {
        override fun streamData(accessor: BlockAccessor): List<HTExperienceView> =
            HTExperienceCapabilities.getCapabilityViews(accessor.level, accessor.position, accessor.side).map(::HTExperienceView)
    }

    data object ForEntity : HTExperienceHandlerProvider<EntityAccessor>() {
        override fun streamData(accessor: EntityAccessor): List<HTExperienceView> =
            HTExperienceCapabilities.getCapabilityViews(accessor.entity, null).map(::HTExperienceView)
    }
}
