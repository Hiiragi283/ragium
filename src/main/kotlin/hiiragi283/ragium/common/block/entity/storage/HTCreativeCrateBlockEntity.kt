package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTMutableItemSlot
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeCrateBlockEntity(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(RagiumBlockEntityTypes.CREATIVE_CRATE, pos, state) {
    private var item: HTItemResourceType? = null

    override fun createSlot(listener: HTContentListener): HTMutableItemSlot = CreativeItemSlotN()

    private inner class CreativeItemSlotN :
        HTMutableItemSlot(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        override fun setResource(resource: HTItemResourceType?) {
            item = resource
        }

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = Int.MAX_VALUE

        override fun getResource(): HTItemResourceType? = item

        override fun getCapacity(resource: HTItemResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTItemResourceType): Boolean = false
    }
}
