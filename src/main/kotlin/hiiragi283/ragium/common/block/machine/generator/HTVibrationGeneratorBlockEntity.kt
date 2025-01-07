package hiiragi283.ragium.common.block.machine.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.GameEventTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.BlockPositionSource
import net.minecraft.world.event.GameEvent
import net.minecraft.world.event.PositionSource
import net.minecraft.world.event.Vibrations
import net.minecraft.world.event.listener.GameEventListener

class HTVibrationGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.VIBRATION_GENERATOR, pos, state),
    GameEventListener.Holder<Vibrations.VibrationListener>,
    Vibrations {
    override val machineKey: HTMachineKey = RagiumMachineKeys.VIBRATION_GENERATOR

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.VIBRATION_DATA.writeTo(nbt, wrapperLookup, listenerData)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.VIBRATION_DATA.readAndSet(nbt, wrapperLookup, this::listenerData)
    }

    private var vibrating: Boolean = false

    override fun tickEach(world: World, pos: BlockPos, state: BlockState, ticks: Int) {
        Vibrations.Ticker.tick(world, listenerData, callback)
    }
    
    override fun process(world: World, pos: BlockPos) {
        if (!vibrating) throw HTMachineException.GenerateEnergy(false)
    }

    override fun onSucceeded(world: World, pos: BlockPos) {
        vibrating = false
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    //    GameEventListener    //

    private val listener = Vibrations.VibrationListener(this)

    override fun getEventListener(): Vibrations.VibrationListener = listener

    //    Vibrations    //

    private var listenerData = Vibrations.ListenerData()
    private val callback = Callback()

    override fun getVibrationListenerData(): Vibrations.ListenerData = listenerData

    override fun getVibrationCallback(): Vibrations.Callback = callback

    private inner class Callback : Vibrations.Callback {
        private val positionSource = BlockPositionSource(pos)

        override fun getRange(): Int = 8

        override fun getPositionSource(): PositionSource = positionSource

        override fun getTag(): TagKey<GameEvent> = GameEventTags.SHRIEKER_CAN_LISTEN

        override fun accepts(
            world: ServerWorld,
            pos: BlockPos,
            event: RegistryEntry<GameEvent>,
            emitter: GameEvent.Emitter,
        ): Boolean = !isActive

        override fun accept(
            world: ServerWorld,
            pos: BlockPos,
            event: RegistryEntry<GameEvent>,
            sourceEntity: Entity?,
            entity: Entity?,
            distance: Float,
        ) {
            this@HTVibrationGeneratorBlockEntity.vibrating = true
        }

        override fun onListen() {
            this@HTVibrationGeneratorBlockEntity.markDirty()
        }

        override fun requiresTickingChunksAround(): Boolean = true
    }
}
