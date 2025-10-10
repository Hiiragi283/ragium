package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.isOf
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Direction
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.attachment.IAttachmentHolder

class HTMachineUpgradeItemHandler private constructor(private val listener: HTContentListener?) :
    HTItemHandler,
    HTValueSerializable {
        companion object {
            @JvmStatic
            fun fromHolder(holder: IAttachmentHolder): HTMachineUpgradeItemHandler =
                HTMachineUpgradeItemHandler(checkNotNull(holder as? BlockEntity)::setChanged)

            @JvmStatic
            fun getComponentTier(stack: HTItemStorageStack): HTComponentTier? = RagiumItems.COMPONENTS
                .toList()
                .firstOrNull { (_, item: ItemLike) -> stack.isOf(item) }
                ?.first
        }

        private val slots: List<HTItemSlot> = (0..3).map { i ->
            HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(8),
                HTSlotHelper.getSlotPosY(i - 0.5),
                canExtract = HTItemStackSlot.MANUAL_ONLY,
                canInsert = HTItemStackSlot.MANUAL_ONLY,
                filter = { stack: HTItemStorageStack ->
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

        fun getTier(): HTComponentTier? = getItemSlot(3, getItemSideFor())?.getStack()?.let(::getComponentTier)
    }
