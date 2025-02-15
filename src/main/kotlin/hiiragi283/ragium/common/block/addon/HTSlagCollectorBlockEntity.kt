package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineType
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
    private val itemHandler: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    private val serializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(listOf(itemHandler.createSlot(0)))

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.readNbt(nbt, dynamicOps)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemHandler.dropStacks(level, pos)
    }

    fun onReceiveEvent(event: HTMachineProcessEvent.Success) {
        if (event.machine.machineType != HTMachineType.BLAST_FURNACE) return
        itemHandler.insertItem(0, RagiumItems.SLAG.toStack(), false)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemHandler(itemHandler)
}
