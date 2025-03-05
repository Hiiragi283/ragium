package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTErrorHoldingBlockEntity
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTErrorMessageProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val SHOW_MESSAGE: MapCodec<Unit> = Codec.unit(Unit).fieldOf("show_message")

    @JvmField
    val MESSAGE: MapCodec<String> = Codec.STRING.fieldOf("message")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val blockEntity: HTErrorHoldingBlockEntity = accessor.blockEntity as? HTErrorHoldingBlockEntity ?: return
        accessor.writeData(SHOW_MESSAGE, Unit)
        val message: String = blockEntity.getErrorMessage() ?: return
        accessor.writeData(MESSAGE, message)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("error_message")

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        if (accessor.readData(SHOW_MESSAGE).isEmpty) return
        accessor
            .readData(MESSAGE)
            .ifPresentOrElse(
                { message: String ->
                    tooltip.add(Component.translatable(message).withStyle(ChatFormatting.RED))
                },
                {
                    tooltip.add(
                        Component
                            .translatable(
                                RagiumTranslationKeys.MACHINE_WORKING_SUCCESS,
                            ).withStyle(ChatFormatting.GREEN),
                    )
                },
            )
    }
}
