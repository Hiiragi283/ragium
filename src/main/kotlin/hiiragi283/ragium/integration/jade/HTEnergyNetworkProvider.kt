package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.energy.HTEnergyNetwork
import hiiragi283.ragium.api.energy.energyNetwork
import hiiragi283.ragium.api.extension.asServerLevel
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.BoxStyle
import snownee.jade.api.ui.IElementHelper
import java.text.NumberFormat
import kotlin.jvm.optionals.getOrNull

object HTEnergyNetworkProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val CODEC: MapCodec<Int> = Codec.INT.fieldOf("amount")

    //    IServerDataProvider    //

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val network: HTEnergyNetwork = accessor.level.asServerLevel()?.energyNetwork ?: return
        accessor.writeData(CODEC, network.energyStored)
    }

    override fun getUid(): ResourceLocation? = RagiumAPI.id("energy_network")

    //    IComponentProvider    //

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val amount: Int = accessor.readData(CODEC).getOrNull() ?: return
        val helper: IElementHelper = IElementHelper.get()
        tooltip.add(
            helper
                .progress(
                    amount / Int.MAX_VALUE.toFloat(),
                    Component.literal("${NumberFormat.getNumberInstance().format(amount)} FE"),
                    helper.progressStyle().color(-5636096, -10092544),
                    BoxStyle.getNestedBox(),
                    true,
                ),
        )
    }
}
