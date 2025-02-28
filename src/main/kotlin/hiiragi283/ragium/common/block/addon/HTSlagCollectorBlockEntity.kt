package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTSlagCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.SLAG_COLLECTOR, pos, state),
    HTHandlerBlockEntity {
    private val itemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .setValidator { variant: HTItemVariant -> variant.isIn(RagiumItemTags.SLAG) }
        .build("item")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemSlot.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemSlot.readNbt(nbt, dynamicOps)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemSlot.dropStack(level, pos)
    }

    fun onReceiveEvent(event: HTMachineProcessEvent.Success) {
        if (event.machine.machineType != HTMachineType.BLAST_FURNACE) return
        itemSlot.insert(HTItemVariant.of(RagiumItems.SLAG), 1, false)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemSlot(itemSlot)
}
