package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.sendUpdatePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Supplier

abstract class HTBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state) {
    companion object {
        const val ACTIVE_KEY = "isActive"
        const val ENCH_KEY = "enchantment"
        const val OWNER_KEY = "owner"
    }

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        CompoundTag().apply { saveAdditional(this, registries) }

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.handleUpdateTag(tag, lookupProvider)
        loadAdditional(tag, lookupProvider)
    }

    override fun setChanged() {
        super.setChanged()
        sendUpdatePacket()
    }

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        writeNbt(tag, registries.createSerializationContext(NbtOps.INSTANCE))
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        readNbt(tag, registries.createSerializationContext(NbtOps.INSTANCE))
    }

    //    Extension    //

    protected open fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {}

    protected open fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {}

    /**
     * ブロックが右クリックされたときに呼ばれます。
     *
     * [onRightClicked]より先に呼び出されます。
     */
    open fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

    /**
     * ブロックが右クリックされたときに呼ばれます。
     */
    open fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = state.getMenuProvider(level, pos)?.let { provider: MenuProvider ->
        when (level.isClientSide) {
            true -> InteractionResult.SUCCESS
            else -> {
                openMenu(player, provider)
                InteractionResult.CONSUME
            }
        }
    } ?: InteractionResult.PASS

    protected open fun openMenu(player: Player, provider: MenuProvider) {
        player.openMenu(provider)
    }

    /**
     * ブロックが左クリックされたときに呼ばれます。
     */
    open fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {}

    /**
     * ブロックが設置されたときに呼ばれます。
     */
    open fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {}

    /**
     * ブロックが置換されたときに呼ばれます。
     */
    open fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
    }

    /**
     * ブロックのコンパレータ出力を返します。
     */
    open fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    open fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
    }

    open val shouldTick: Boolean = true
    open var ticks: Int = 0
        protected set
    open val tickRate: Int = 200

    /**
     * 毎tick呼び出されます。
     * @see [hiiragi283.ragium.common.block.HTEntityBlock.getTicker]
     */
    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (!shouldTick) return
        tickEach(level, pos, state, ticks)
        if (ticks >= tickRate) {
            tickSecond(level, pos, state)
            ticks = 0
        } else {
            ticks++
        }
    }

    /**
     * 毎[tick]呼び出されます。
     */
    open fun tickEach(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
    }

    /**
     * [ticks]が[tickRate]以上の値となったときに呼び出されます。
     */
    open fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {}
}
