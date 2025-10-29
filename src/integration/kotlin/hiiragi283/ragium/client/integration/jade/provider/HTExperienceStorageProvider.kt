package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.integration.jade.HTBasicJadeDataProvider
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.longText
import net.minecraft.ChatFormatting
import snownee.jade.api.Accessor
import snownee.jade.api.BlockAccessor
import snownee.jade.api.EntityAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

sealed class HTExperienceStorageProvider<ACCESSOR : Accessor<*>> :
    HTBasicJadeDataProvider<ACCESSOR, HTExperienceView>(
        "experience_storage",
        HTExperienceView.CODEC.cast(),
    ) {
    final override fun appendTooltip(
        tooltip: ITooltip,
        accessor: ACCESSOR,
        config: IPluginConfig,
        data: HTExperienceView,
    ) {
        tooltip.add(RagiumTranslation.JADE_EXP_STORAGE.getComponent(longText(data.getAmount()).withStyle(ChatFormatting.WHITE)))
    }

    data object ForBlocks : HTExperienceStorageProvider<BlockAccessor>() {
        override fun streamData(accessor: BlockAccessor): HTExperienceView? =
            RagiumCapabilities.EXPERIENCE.getCapability(accessor.level, accessor.position, accessor.side)?.let(::HTExperienceView)
    }

    data object ForEntity : HTExperienceStorageProvider<EntityAccessor>() {
        override fun streamData(accessor: EntityAccessor): HTExperienceView? =
            RagiumCapabilities.EXPERIENCE.getCapability(accessor.entity, null)?.let(::HTExperienceView)
    }
}
