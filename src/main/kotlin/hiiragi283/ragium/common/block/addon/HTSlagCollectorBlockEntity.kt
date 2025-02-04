package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler

class HTSlagCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.SLAG_COLLECTOR, pos, state),
    HTBlockEntityHandlerProvider {
    private val itemHandler: ItemStackHandler = object : ItemStackHandler(1) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = stack.`is`(RagiumItemTags.SLAG)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_OUTPUT_KEY, itemHandler.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_OUTPUT_KEY))
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

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): ItemStackHandler = itemHandler
}
