package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.readNbt
import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.extension.sendS2CPacket
import hiiragi283.ragium.api.extension.writeNbt
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    companion object {
        const val MACHINE_KEY = "machine"
        const val TIER_KEY = "tier"
    }

    open fun asInventory(): SidedInventory? = null

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.readNbt(nbt, wrapperLookup)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(registryLookup)

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)

    override fun markDirty() {
        super.markDirty()
        syncInventory()
    }

    //    Extensions    //

    open fun syncInventory() {
        sendPacket { asInventory()?.sendS2CPacket(it, pos) }
    }

    fun <T> ifPresentWorld(action: (World) -> T): T? = world?.let(action)

    open fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = state.createScreenHandlerFactory(world, pos)?.let {
        when (world.isClient) {
            true -> ActionResult.SUCCESS
            else -> {
                player.openHandledScreen(it)
                ActionResult.CONSUME
            }
        }
    } ?: ActionResult.PASS

    open fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        asInventory()?.let { ItemScatterer.spawn(world, pos, it) }
    }
    
    open fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = 0

    open val shouldTick: Boolean = true
    open var ticks: Int = 0
        protected set
    open val tickRate: Int = 200

    fun tick(world: World, pos: BlockPos, state: BlockState) {
        if (!shouldTick) return
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

    protected fun createContext(): ScreenHandlerContext = ifPresentWorld { world: World ->
        ScreenHandlerContext.create(world, pos)
    } ?: ScreenHandlerContext.EMPTY
}
