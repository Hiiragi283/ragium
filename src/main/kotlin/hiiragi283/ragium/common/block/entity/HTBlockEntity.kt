package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTOwnedBlockEntity
import hiiragi283.lib.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.lib.item.HTItemDropHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.HTHandlerProvider
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.ItemResourceHandler
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.resolver.HTResourceCapabilityManager
import hiiragi283.ragium.common.transfer.HTCapabilityCodec
import java.util.UUID
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [HTExtendedBlockEntity]の拡張クラスです。
 *
 * 参考 : [Mekanism - TileEntityMekanism](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/tile/base/TileEntityMekanism.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBlockEntity(type: BlockEntityType<*>, worldPosition: BlockPos, blockState: BlockState) :
    HTExtendedBlockEntity(type, worldPosition, blockState),
    Nameable,
    HTHandlerProvider,
    HTOwnedBlockEntity,
    HTSoundPlayerBlockEntity {
    //    Ticking    //

    companion object {
        @JvmStatic
        fun tickClient(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            blockEntity.onUpdateClient(level, pos, state)
            blockEntity.ticks++
        }

        @JvmStatic
        fun tickServer(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            val shouldUpdate: Boolean = blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
            if (shouldUpdate) {
                blockEntity.sendUpdatePacket(serverLevel)
            }
        }
    }

    var ticks: Int = 0
        protected set

    /**
     * クライアント側でのティック処理を行います。
     */
    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    /**
     * サーバー側でのティック処理を行います。
     * @return クライアント側へ更新を同期する場合は`true`
     */
    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Save & Read    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.saveTo(output, this)
            }
        }
        // Custom Name
        output.storeNullable("custom_name", HTCodecs.TEXT, this.customName)
        // Owner
        output.storeNullable(HTConstants.OWNER, HTCodecs.UUID, ownerId)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.loadFrom(input, this)
            }
        }
        // Custom Name
        input.read("custom_name", HTCodecs.TEXT).ifPresent(::customName::set)
        // Owner
        input.read(HTConstants.OWNER, HTCodecs.UUID).ifPresent(::ownerId::set)
    }

    //    Nameable    //

    private var customName: Text? = null

    final override fun getName(): Text = customName ?: blockState.block.name

    final override fun getCustomName(): Text? = customName

    //    HTOwnedBlockEntity    //

    final override var ownerId: UUID? = null

    //    Capability    //

    protected val fluidHandlerManager: HTResourceCapabilityManager<FluidResource, HTFluidTank>?
    protected val itemHandlerManager: HTResourceCapabilityManager<ItemResource, HTItemSlot>?

    init {
        initializeVariables(::setOnlySave)
        fluidHandlerManager = createFluidHandler(::setOnlySave)?.let(::HTResourceCapabilityManager)
        itemHandlerManager = createItemHandler(::setOnlySave)?.let(::HTResourceCapabilityManager)
    }

    /**
     * [fluidHandlerManager]や[itemHandlerManager]が初期化される前に変数を初期化します。
     */
    protected open fun initializeVariables(listener: Runnable) {}

    // Fluid
    fun hasFluidHandler(): Boolean = fluidHandlerManager != null

    protected open fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? = null

    fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: emptyList()

    fun getFluidTank(side: Direction?, index: Int): HTFluidTank? = getFluidTanks(side).getOrNull(index)

    final override fun getFluidHandler(direction: Direction?): FluidResourceHandler? = fluidHandlerManager?.resolve(direction)

    // Item
    fun hasItemHandler(): Boolean = itemHandlerManager != null

    protected open fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? = null

    fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: emptyList()

    fun getItemSlot(side: Direction?, index: Int): HTItemSlot? = getItemSlots(side).getOrNull(index)

    final override fun getItemHandler(direction: Direction?): ItemResourceHandler? = itemHandlerManager?.resolve(direction)

    override fun preRemoveSideEffects(pos: BlockPos, state: BlockState) {
        val level: Level = this.level ?: return
        onBlockRemoved(state, level, pos)
    }

    /**
     * ブロックが削除されたときに呼び出されます。
     */
    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {
        if (shouldDrop(state, level, pos)) {
            for (slot: HTItemSlot in getItemSlots(null)) {
                HTItemDropHelper.dropStackAt(level, pos, slot.getItemStack())
            }
        }
    }

    /**
     * アイテムをドロップするかどうか判定します。
     */
    protected open fun shouldDrop(state: BlockState, level: Level, pos: BlockPos): Boolean = true
}
