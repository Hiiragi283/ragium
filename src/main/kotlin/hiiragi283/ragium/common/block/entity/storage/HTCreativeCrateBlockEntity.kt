package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toStackOrEmpty
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.impl.storage.item.HTItemStackResourceSlot
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTCreativeCrateBlockEntity(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(RagiumBlockEntityTypes.CREATIVE_CRATE, pos, state) {
    override fun createSlot(listener: HTContentListener): HTItemStackResourceSlot = CreativeItemSlot()

    override fun isCreative(): Boolean = true

    override fun createSlotWidget(): HTItemSlotWidget = HTItemSlotWidget
        .fake(
            slot,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.NONE,
        ).setGhost()

    private inner class CreativeItemSlot :
        HTItemStackResourceSlot(),
        HTContentListener.Empty {
        private var item: HTItemResourceType? = null

        override fun getStack(): ItemStack = item.toStackOrEmpty(Int.MAX_VALUE)

        override fun setStack(stack: ItemStack) {
            setStackInternal(stack)
        }

        override fun setStackInternal(stack: ItemStack) {
            item = getResourceFrom(stack)
            setChanged()
        }

        override fun updateAmount(newAmount: Int) {}

        override fun isValid(resource: HTItemResourceType): Boolean = true

        override fun getCapacity(resource: HTItemResourceType?): Int = Int.MAX_VALUE

        override fun serialize(output: HTValueOutput) {
            output.write(HTConst.ITEM, HTItemResourceType.CODEC, item)
        }

        override fun deserialize(input: HTValueInput) {
            item = input.read(HTConst.ITEM, HTItemResourceType.CODEC)
        }
    }
}
