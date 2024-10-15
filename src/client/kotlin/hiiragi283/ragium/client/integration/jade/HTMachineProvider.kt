package hiiragi283.ragium.client.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElementHelper
import kotlin.jvm.optionals.getOrNull

object HTMachineProvider : IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    @JvmField
    val TYPE: MapCodec<HTMachineType> = HTMachineTypeRegistry.CODEC.fieldOf("type")

    @JvmField
    val TIER: MapCodec<HTMachineTier> = HTMachineTier.CODEC.fieldOf("tier")

    @JvmField
    val INVENTORY: MapCodec<HTSimpleInventory> = HTSimpleInventory.CODEC.fieldOf("inventory")

    @JvmField
    val TICK: MapCodec<Int> = Codec.INT.fieldOf("tick")

    @JvmField
    val MAX_TICK: MapCodec<Int> = Codec.INT.fieldOf("maxTick")

    @JvmField
    val PREVIEW: MapCodec<Boolean> = Codec.BOOL.fieldOf("preview")

    //    IBlockComponentProvider    //

    override fun getUid(): Identifier = RagiumJadePlugin.MACHINE

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val type: HTMachineType = accessor
            .readData(TYPE)
            .getOrNull()
            ?: return
        val tier: HTMachineTier = accessor
            .readData(TIER)
            .orElse(HTMachineTier.PRIMITIVE)
        type.appendTooltip(
            tooltip::add,
            tier,
        )

        val progress: Int = accessor.readData(TICK).orElse(0)
        val maxProgress: Int = accessor.readData(MAX_TICK).orElse(tier.tickRate)
        val helper: IElementHelper = IElementHelper.get()
        accessor.readData(INVENTORY).ifPresent { inventory: HTSimpleInventory ->
            if (!inventory.isEmpty) {
                tooltip.add(helper.item(inventory.getStack(0)))
                tooltip.append(helper.item(inventory.getStack(1)))
                tooltip.append(helper.item(inventory.getStack(2)))
                tooltip.append(helper.spacer(4, 0))
                tooltip.append(helper.progress(progress.toFloat() / maxProgress.toFloat()).translate(Vec2f(-2.0f, 0.0f)))
                tooltip.append(helper.item(inventory.getStack(4)))
                tooltip.append(helper.item(inventory.getStack(5)))
                tooltip.append(helper.item(inventory.getStack(6)))
            }
        }

        val showPreview: Boolean = accessor.readData(PREVIEW).getOrNull() ?: return
        tooltip.add(Text.literal("Show Preview: $showPreview"))
    }

    //    IServerDataProvider    //

    override fun appendServerData(nbt: NbtCompound, accessor: BlockAccessor) {
        accessor.machineEntity?.let { machine: HTMachineEntity ->
            accessor.writeData(TYPE, machine.machineType)
            accessor.writeData(TIER, machine.tier)
            accessor.writeData(INVENTORY, machine.parent)
            accessor.writeData(TICK, machine.propertyDelegate.get(0))
            accessor.writeData(MAX_TICK, machine.propertyDelegate.get(1))
            if (machine is HTMultiblockController) {
                accessor.writeData(PREVIEW, machine.showPreview)
            }
        }
    }
}
