package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import kotlin.jvm.optionals.getOrNull

object HTMachineInfoProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val IS_ACTIVE: MapCodec<Boolean> = Codec.BOOL.fieldOf("is_active")

    @JvmField
    val SHOW_PREVIEW: MapCodec<Boolean> = Codec.BOOL.fieldOf("show_preview")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val machineEntity: HTMachineBlockEntity = accessor.blockEntity as? HTMachineBlockEntity ?: return
        accessor.writeData(HTMachineKey.FIELD_CODEC, machineEntity.machineKey)
        accessor.writeData(HTMachineTier.FIELD_CODEC, machineEntity.machineTier)
        accessor.writeData(IS_ACTIVE, machineEntity.isActive)
        accessor.writeData(SHOW_PREVIEW, machineEntity.showPreview)
    }

    override fun getUid(): ResourceLocation? = RagiumAPI.id("machine_info")

    //    IComponentProvider    //

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val machineKey: HTMachineKey = accessor.readData(HTMachineKey.FIELD_CODEC).getOrNull() ?: return
        val tier: HTMachineTier = accessor.readData(HTMachineTier.FIELD_CODEC).getOrNull() ?: return
        machineKey.appendTooltip(tooltip::add, tier, false)
        val isActive: Boolean = accessor.readData(IS_ACTIVE).orElse(false)
        tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_WORKING, boolText(isActive)))
        val showPreview: Boolean = accessor.readData(SHOW_PREVIEW).orElse(false)
        tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_PREVIEW, boolText(showPreview)))
    }
}
