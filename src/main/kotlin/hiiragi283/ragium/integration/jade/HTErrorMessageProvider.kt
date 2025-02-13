package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTErrorHoldingBlockEntity
import hiiragi283.ragium.api.extension.identifyFunction
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import java.util.Optional

object HTErrorMessageProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val CODEC: MapCodec<Optional<String>> = Codec.STRING.optionalFieldOf("message")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val blockEntity: HTErrorHoldingBlockEntity = accessor.blockEntity as? HTErrorHoldingBlockEntity ?: return
        accessor.writeData(CODEC, Optional.ofNullable(blockEntity.getErrorMessage()))
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("error_message")

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        accessor.readData(CODEC).flatMap(identifyFunction()).ifPresent { message: String ->
            tooltip.add(Component.literal(message).withStyle(ChatFormatting.RED))
        }
    }
}
