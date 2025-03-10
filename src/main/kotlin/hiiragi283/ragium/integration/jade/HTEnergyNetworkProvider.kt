package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.energy.IEnergyStorage
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.BoxStyle
import snownee.jade.api.ui.IElementHelper
import kotlin.jvm.optionals.getOrNull

object HTEnergyNetworkProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val CODEC: MapCodec<Int> = Codec.INT.fieldOf("amount")

    //    IServerDataProvider    //

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val network: IEnergyStorage = RagiumAPI.getInstance().getEnergyNetwork(accessor.level) ?: return
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
                    Component.translatable(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, intText(amount)),
                    helper.progressStyle().color(-5636096, -10092544),
                    BoxStyle.getNestedBox(),
                    true,
                ),
        )
    }
}
