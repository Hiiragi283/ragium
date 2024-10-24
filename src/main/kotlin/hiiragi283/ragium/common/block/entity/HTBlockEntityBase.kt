package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    protected open fun asInventory(): HTSimpleInventory? = (this as? HTDelegatedInventory<*>)?.parent

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.readNbt(nbt, registryLookup)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(registryLookup)

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)

    override fun markDirty() {
        super.markDirty()
        sendS2CPacket()
    }

    //    Extensions    //

    fun sendS2CPacket() {
        sendPacket { asInventory()?.sendS2CPacket(it, pos) }
    }

    open fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when (world.isClient) {
        true -> ActionResult.SUCCESS
        else -> {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
            ActionResult.CONSUME
        }
    }

    open fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = 0

    open var ticks: Int = 0
        protected set
    open val tickRate: Int = 200

    fun tick(world: World, pos: BlockPos, state: BlockState) {
        tickEach(world, pos, state, ticks)
        if (ticks >= tickRate) {
            tickSecond(world, pos, state)
            ticks = 0
        } else {
            ticks++
        }
    }

    open fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {}

    open fun tickSecond(world: World, pos: BlockPos, state: BlockState) {}
}
