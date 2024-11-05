package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTMachineBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    SidedStorageBlockEntity {
    companion object {
        const val MACHINE_KEY = "machine"
        const val TIER_KEY = "tier"
    }

    abstract var key: HTMachineKey
        protected set
    var tier: HTMachineTier = HTMachineTier.PRIMITIVE
        protected set(value) {
            val old: HTMachineTier = field
            field = value
            onTierUpdated(old, value)
        }
    val definition: HTMachineDefinition
        get() = HTMachineDefinition(key, tier)
    val packet: HTMachinePacket
        get() = HTMachinePacket(key, tier, pos)

    val facing: Direction
        get() = cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)

    open fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {}

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.putMachineKey(MACHINE_KEY, key)
        nbt.putTier(TIER_KEY, tier)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        key = nbt.getMachineKey(MACHINE_KEY)
        tier = nbt.getTier(TIER_KEY)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        // Insert fluid from holding stack
        if (interactWithFluidStorage(player)) {
            return ActionResult.success(world.isClient)
        }
        // Validate multiblock
        if (this is HTMultiblockController) {
            return onUseController(state, world, pos, player, this)
        }
        // open machine screen
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            else -> {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                ActionResult.CONSUME
            }
        }
    }

    abstract fun interactWithFluidStorage(player: PlayerEntity): Boolean

    final override val tickRate: Int = tier.tickRate

    val property: PropertyDelegate = HTDynamicPropertyDelegate(3, ::getProperty, ::setProperty)

    protected open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> -1
    }

    protected open fun setProperty(index: Int, value: Int) {}

    //    ExtendedScreenHandlerFactory    //

    final override fun getDisplayName(): Text = tier.createPrefixedText(key)

    final override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = HTMachinePacket(key, tier, pos)
}
