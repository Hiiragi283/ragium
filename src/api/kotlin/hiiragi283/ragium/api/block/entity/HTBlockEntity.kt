package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentHolder
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.getData
import hiiragi283.ragium.api.extension.putData
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTEnchantmentHolder,
    HTNbtCodec {
    //    Save & Load    //

    /**
     * クライアント側へ同期する際に送る[CompoundTag]
     */
    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveCustomOnly(registries)

    /**
     * クライアント側に同期パケットを送る
     */
    fun sendUpdatePacket(serverLevel: ServerLevel) {
        RagiumAPI.getInstance().sendUpdatePayload(this, serverLevel)
    }

    /**
     * クライアント側でパケットを受け取った時の処理
     */
    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val registryOps: RegistryOps<Tag> = registries.createSerializationContext(NbtOps.INSTANCE)
        // Enchantment
        if (!itemEnchantments.isEmpty) {
            tag.putData(RagiumConstantValues.ENCHANTMENT, itemEnchantments, ItemEnchantments.CODEC, registryOps)
        }
        // Custom
        writeNbt(tag, registryOps)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val registryOps: RegistryOps<Tag> = registries.createSerializationContext(NbtOps.INSTANCE)
        // Enchantment
        tag
            .getData(RagiumConstantValues.ENCHANTMENT, ItemEnchantments.CODEC, registryOps)
            .result()
            .orElse(ItemEnchantments.EMPTY)
            .let(::loadEnchantment)
        // Custom
        readNbt(tag, registryOps)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        loadEnchantment(componentInput.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY))
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(DataComponents.ENCHANTMENTS, itemEnchantments)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        afterUpdateState(blockState)
    }

    final override fun setLevel(level: Level) {
        super.setLevel(level)
        afterLevelInit(level)
    }

    //    Extension    //

    /**
     * [setBlockState]の後で呼び出されます。
     */
    protected open fun afterUpdateState(state: BlockState) {}

    /**
     * [setLevel]の後で呼び出されます。
     */
    protected open fun afterLevelInit(level: Level) {}

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
    ): InteractionResult = InteractionResult.PASS

    /**
     * ブロックが左クリックされたときに呼ばれます。
     */
    open fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
    }

    /**
     * ブロックが設置されたときに呼ばれます。
     */
    open fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
    }

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

    /**
     * このブロックエンティティが生成されてからの経過時間
     *
     * セーブのたびにリセットされる
     */
    var totalTick: Int = 0
        protected set

    /**
     * エンチャントが更新されたときに呼び出されます。
     */
    protected open fun loadEnchantment(newEnchantments: ItemEnchantments) {
        this.itemEnchantments = newEnchantments
    }

    //    Capability    //

    /**
     * 指定した[direction]から[IItemHandler]を返します。
     */
    open fun getItemHandler(direction: Direction?): IItemHandler? = this as? HTItemSlotHandler

    /**
     * 指定した[direction]から[IFluidHandler]を返します。
     */
    open fun getFluidHandler(direction: Direction?): IFluidHandler? = this as? HTFluidTankHandler

    /**
     * 指定した[direction]から[IEnergyStorage]を返します。
     */
    open fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null

    //    HTEnchantmentHolder    //

    protected var itemEnchantments: ItemEnchantments = ItemEnchantments.EMPTY
        private set

    final override fun getEnchLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        return lookup.get(key).map(itemEnchantments::getLevel).orElse(0)
    }

    final override fun getEnchEntries(): Iterable<EnchantmentInstance> =
        itemEnchantments.entrySet().map { (key: Holder<Enchantment>, value: Int) -> EnchantmentInstance(key, value) }
}
