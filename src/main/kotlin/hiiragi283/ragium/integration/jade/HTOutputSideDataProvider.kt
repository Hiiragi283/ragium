package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.integration.jade.base.HTBlockDataProvider
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTOutputSideDataProvider : HTBlockDataProvider<Direction>() {
    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: Direction,
    ) {
        tooltip.add(RagiumTranslation.JADE_OUTPUT_SIDE.getComponent(Component.translatable("jade.${data.serializedName}")))
    }

    override fun streamData(accessor: BlockAccessor): Direction? = null

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, Direction> = Direction.STREAM_CODEC.cast()

    override fun getUid(): ResourceLocation = RagiumAPI.id("output_side")
}
