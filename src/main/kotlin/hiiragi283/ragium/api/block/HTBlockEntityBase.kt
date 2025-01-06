package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.extension.readNbt
import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.extension.writeNbt
import hiiragi283.ragium.common.network.HTInventorySyncPayload
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Ragiumで使用する[BlockEntity]クラスの基礎
 * @see [HTMachineBlockEntityBase]
 */
abstract class HTBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state) {
    companion object {
        const val FLUID_KEY = "fluid_storage"
        const val ITEM_KEY = "item_storage"
    }

    /**
     * 外部に[SidedInventory]を公開します。
     */
    open fun asInventory(): SidedInventory? = null

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        asInventory()?.readNbt(nbt, wrapperLookup)
    }

    final override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(registryLookup)

    final override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)

    final override fun markDirty() {
        super.markDirty()
        syncInventory()
    }

    //    Extensions    //

    /**
     * [asInventory]で取得した[SidedInventory]をクライアント側に同期します。
     * @see [markDirty]
     */
    fun syncInventory() {
        val inventory: SidedInventory = asInventory() ?: return
        sendPacket(HTInventorySyncPayload(pos, inventory))
    }

    /**
     * ブロックが右クリックされたときに呼ばれます。
     * @see [HTBlockWithEntity.onUse]
     */
    open fun onRightClicked(
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

    /**
     * ブロックが左クリックされたときに呼ばれます。
     * @see [HTBlockWithEntity.onBlockBreakStart]
     */
    open fun onLeftClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {}

    /**
     * ブロックが設置されたときに呼ばれます。
     * @see [HTBlockWithEntity.onPlaced]
     */
    open fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
    }

    /**
     * ブロックが置換されたときに呼ばれます。
     * @see [HTBlockWithEntity.onStateReplaced]
     */
    open fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        asInventory()?.let { ItemScatterer.spawn(world, pos, it) }
    }

    /**
     * ブロックのコンパレータ出力を返します。
     * @see [HTBlockWithEntity.getComparatorOutput]
     */
    open fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = 0

    open val shouldTick: Boolean = true
    open var ticks: Int = 0
        protected set
    open val tickRate: Int = 200

    /**
     * 毎tick呼び出されます。
     * @see [HTBlockWithEntity.getTicker]
     */
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

    /**
     * 毎[tick]呼び出されます。
     */
    open fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
    }

    /**
     * [ticks]が[tickRate]以上の値となったときに呼び出されます。
     */
    open fun tickSecond(world: World, pos: BlockPos, state: BlockState) {}
}
