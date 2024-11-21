package hiiragi283.ragium.api.machine.block

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.Unit

abstract class HTMachineBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    SidedStorageBlockEntity {
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

    @Environment(EnvType.CLIENT)
    open fun onPacketReceived(packet: HTMachinePacket) {
        key = packet.key
        tier = packet.tier
    }

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

    val isActive: Boolean
        get() = cachedState.getOrDefault(RagiumBlockProperties.ACTIVE, false)

    protected fun activateState(world: World, pos: BlockPos, newState: Boolean) {
        if (!world.isClient) {
            world.modifyBlockState(pos) { stateIn: BlockState ->
                if (stateIn.contains(RagiumBlockProperties.ACTIVE)) {
                    if (stateIn.get(RagiumBlockProperties.ACTIVE) == newState) {
                        null
                    } else {
                        stateIn.with(RagiumBlockProperties.ACTIVE, newState)
                    }
                } else {
                    null
                }
            }
        }
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

    final override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        getRequiredEnergy(world, pos)
            .flatMap { (flag: HTEnergyNetwork.Flag, amount: Long) ->
                val network: HTEnergyNetwork = world.energyNetwork
                    ?: return@flatMap DataResult.error { "Failed to find energy network!" }
                when (flag) {
                    HTEnergyNetwork.Flag.CONSUME -> when (network.canConsume(amount)) {
                        true -> DataResult.success(Triple(network, flag, amount))
                        false -> DataResult.error { "Failed to extract required energy from network!" }
                    }

                    HTEnergyNetwork.Flag.GENERATE -> DataResult.success(Triple(network, flag, amount))
                }
            }.flatMap { (network: HTEnergyNetwork, flag: HTEnergyNetwork.Flag, amount: Long) ->
                if (process(world, pos) && flag.processAmount(network, amount)) {
                    key.entry.ifPresent(HTMachinePropertyKeys.SOUND) {
                        world.playSound(null, pos, it, SoundCategory.BLOCKS, 0.2f, 1.0f)
                    }
                    activateState(world, pos, true)
                    onSucceeded(world, pos)
                    DataResult.success(Unit)
                } else {
                    DataResult.error { "Failed to process machine!" }
                }
            }.ifError { _: DataResult.Error<Unit> ->
                activateState(world, pos, false)
                onFailed(world, pos)
            }
    }

    abstract fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>>

    abstract fun process(world: World, pos: BlockPos): Boolean

    open fun onSucceeded(world: World, pos: BlockPos) {}

    open fun onFailed(world: World, pos: BlockPos) {}

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
