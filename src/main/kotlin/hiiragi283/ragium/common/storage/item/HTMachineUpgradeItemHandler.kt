package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTMachineUpgradeHandler
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.attachment.IAttachmentHolder

internal class HTMachineUpgradeItemHandler(private val listener: HTContentListener?) : HTMachineUpgradeHandler {
    companion object {
        @JvmStatic
        fun fromHolder(holder: IAttachmentHolder): HTMachineUpgradeHandler =
            HTMachineUpgradeItemHandler(checkNotNull(holder as? BlockEntity)::setChanged)

        @JvmStatic
        fun getComponentTier(stack: ItemStack): HTBaseTier? = stack.itemHolder.getData(RagiumDataMaps.TIER)?.tier
    }

    private val slots: List<HTItemSlot> = (0..3).map { i ->
        HTItemStackSlot.create(
            listener,
            HTSlotHelper.getSlotPosX(8),
            HTSlotHelper.getSlotPosX(i) + 1,
            canExtract = HTItemStackSlot.MANUAL_ONLY,
            canInsert = HTItemStackSlot.MANUAL_ONLY,
            filter = { stack: ItemStack ->
                when (i) {
                    3 -> getComponentTier(stack) != null
                    else -> getComponentTier(stack) == null
                }
            },
        )
    }

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun serialize(output: HTValueOutput) {
        HTCapabilityCodec.ITEM.saveTo(output, getItemSlots(getItemSideFor()))
    }

    override fun deserialize(input: HTValueInput) {
        HTCapabilityCodec.ITEM.loadFrom(input, getItemSlots(getItemSideFor()))
    }

    override fun getTier(): HTBaseTier? = getItemSlot(3, getItemSideFor())?.getStack()?.let(::getComponentTier)
}
