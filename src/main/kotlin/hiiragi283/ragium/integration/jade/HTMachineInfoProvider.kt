package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTPlayerOwningBlockEntity
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.api.extension.identifyFunction
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.ItemEnchantments
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTMachineInfoProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val OWNER: MapCodec<Optional<UUID>> = HTPlayerOwningBlockEntity.UUID_CODEC.fieldOf("owner")

    @JvmField
    val IS_ACTIVE: MapCodec<Boolean> = Codec.BOOL.fieldOf("is_active")

    @JvmField
    val TICK_RATE: MapCodec<Int> = Codec.INT.fieldOf("tick_rate")

    @JvmField
    val COST_MODIFIER: MapCodec<Int> = Codec.INT.fieldOf("cost_modifier")

    @JvmField
    val SHOW_PREVIEW: MapCodec<Boolean> = Codec.BOOL.fieldOf("show_preview")

    @JvmField
    val ENCHANTMENT: MapCodec<ItemEnchantments> = ItemEnchantments.CODEC.fieldOf("enchantments")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val machineEntity: HTMachineAccess = accessor.blockEntity as? HTMachineAccess ?: return
        accessor.writeData(HTMachineType.FIELD_CODEC, machineEntity.machineType)
        accessor.writeData(OWNER, Optional.ofNullable(machineEntity.owner?.uuid))
        accessor.writeData(TICK_RATE, machineEntity.containerData.get(1))
        accessor.writeData(COST_MODIFIER, machineEntity.costModifier)
        accessor.writeData(IS_ACTIVE, machineEntity.isActive)
        if (machineEntity is HTMultiblockController) {
            accessor.writeData(SHOW_PREVIEW, machineEntity.showPreview)
        }
        accessor.writeData(ENCHANTMENT, machineEntity.enchantments)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("machine_info")

    //    IComponentProvider    //

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val machineType: HTMachineType = accessor.readData(HTMachineType.FIELD_CODEC).getOrNull() ?: return
        machineType.appendTooltip(tooltip::add, false)

        accessor
            .readData(OWNER)
            .flatMap(identifyFunction())
            .getOrNull()
            ?.let(RagiumAPI.getInstance()::getPlayer)
            ?.displayName
            ?.let { Component.translatable(RagiumTranslationKeys.MACHINE_OWNER, it) }

        val isActive: Boolean = accessor.readData(IS_ACTIVE).orElse(false)
        tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_WORKING, boolText(isActive)))

        val tickRate: Int = accessor.readData(TICK_RATE).orElse(200)
        tooltip.add(
            Component.translatable(
                RagiumTranslationKeys.MACHINE_TICK_RATE,
                intText(tickRate),
                floatText(tickRate / 20f),
            ),
        )

        val costModifier: Int = accessor.readData(COST_MODIFIER).orElse(1)
        tooltip.add(Component.literal("- Cost Modifier: x$costModifier"))

        accessor.readData(SHOW_PREVIEW).ifPresent { showPreview: Boolean ->
            tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_PREVIEW, boolText(showPreview)))
        }

        val enchantments: ItemEnchantments = accessor.readData(ENCHANTMENT).orElse(ItemEnchantments.EMPTY)
        enchantments.addToTooltip(Item.TooltipContext.of(accessor.level), tooltip::add, TooltipFlag.ADVANCED)
    }
}
