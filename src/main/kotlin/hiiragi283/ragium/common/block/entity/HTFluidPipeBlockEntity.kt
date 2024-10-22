package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.modifyBlockState
import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.common.block.HTFluidPipeBlock
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.block.ConnectingBlock
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFluidPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.FLUID_PIPE, pos, state),
    HTDelegatedInventory<HTSimpleInventory> {
    fun canConnect(dir: Direction): Boolean = ifPresentWorld { world: World ->
        HTFluidPipeBlock.canConnect(world, pos, dir)
    } ?: false

    override val tickRate: Int
        get() = 20

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        if (world.isClient) return
        // update connection
        world.modifyBlockState(pos) {
            Direction.entries.forEach { dir: Direction ->
                state.with(ConnectingBlock.FACING_PROPERTIES[dir], HTFluidPipeBlock.canConnect(world, pos, dir))
            }
            state
        }
        // move variants
        val dir: Direction = state.get(Properties.FACING)
        val posTo: BlockPos = pos.offset(dir)
        ItemStorage.SIDED
            .find(world, posTo, dir.opposite)
            ?.let { storageTo: Storage<ItemVariant> ->
                StorageUtil.move(
                    InventoryStorage.of(parent, null),
                    storageTo,
                    { true },
                    16,
                    null,
                )
            }
    }

    //    HTDelegatedInventory    //

    override val parent: HTSimpleInventory = HTStorageBuilder(1)
        .set(0, HTStorageIO.GENERIC, HTStorageSide.ANY)
        .buildSided()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }
}
