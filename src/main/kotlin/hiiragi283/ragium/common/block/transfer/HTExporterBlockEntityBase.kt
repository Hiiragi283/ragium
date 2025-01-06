package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumNetworks
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTExporterBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(type, pos, state) {
    var fluidFilter: RegistryEntryList<Fluid> = RegistryEntryList.empty()
    var itemFilter: RegistryEntryList<Item> = RegistryEntryList.empty()

    final override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.FLUID_FILTER.writeTo(nbt, fluidFilter)
        HTNbtCodecs.ITEM_FILTER.writeTo(nbt, itemFilter)
    }

    final override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.FLUID_FILTER.readAndSet(nbt, this::fluidFilter)
        HTNbtCodecs.ITEM_FILTER.readAndSet(nbt, this::itemFilter)
    }

    final override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stack: ItemStack = player.getStackInMainHand()
        val result: Boolean = stack.ifPresent(RagiumComponentTypes.FLUID_FILTER) {
            fluidFilter = it
            true
        } ?: stack.ifPresent(RagiumComponentTypes.ITEM_FILTER) {
            itemFilter = it
            true
        } ?: let {
            if (!world.isClient) {
                player.sendMessage(fluidFilterText(fluidFilter), false)
                player.sendMessage(itemFilterText(itemFilter), false)
            }
            false
        }
        if (result) {
            if (!world.isClient) {
                markDirty()
                RagiumNetworks.sendFloatingItem(
                    player,
                    stack,
                    ParticleTypes.WAX_ON,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                )
            }
        }
        return ActionResult.success(world.isClient)
    }

    protected val front: Direction
        get() = cachedState.get(Properties.FACING)

    final override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        if (world.isReceivingRedstonePower(pos)) return
        // transfer containment
        StorageUtil.move(
            getBackItemStorage(world, pos, front),
            getFrontItemStorage(world, pos, front),
            { itemFilter.isEmpty || itemFilter.any(it::isOf) },
            itemSpeed,
            null,
        )
        StorageUtil.move(
            getBackFluidStorage(world, pos, front),
            getFrontFluidStorage(world, pos, front),
            { fluidFilter.isEmpty || fluidFilter.any(it::isOf) },
            fluidSpeed,
            null,
        )
    }

    abstract val itemSpeed: Long
    abstract val fluidSpeed: Long
}
