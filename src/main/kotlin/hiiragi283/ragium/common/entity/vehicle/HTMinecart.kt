package hiiragi283.ragium.common.entity.vehicle

import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.AbstractMinecart
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [blusunrize.immersiveengineering.common.entities.IEMinecartEntity]
 */
abstract class HTMinecart<BE : HTBlockEntity> :
    AbstractMinecart,
    HTHandlerProvider {
    companion object {
        @JvmField
        val DATA_BLOCK_ENTITY: EntityDataAccessor<CompoundTag> = SynchedEntityData.defineId(
            HTMinecart::class.java,
            EntityDataSerializers.COMPOUND_TAG,
        )
    }

    constructor(entityType: EntityType<*>, level: Level) : super(entityType, level) {
        this.blockEntity = createBlockEntity()
    }

    constructor(entityType: EntityType<*>, level: Level, x: Double, y: Double, z: Double) : super(
        entityType,
        level,
        x,
        y,
        z,
    ) {
        this.blockEntity = createBlockEntity()
    }

    protected var blockEntity: BE
        private set

    protected abstract fun createBlockEntity(pos: BlockPos, state: BlockState): BE

    protected fun createBlockEntity(): BE = createBlockEntity(BlockPos.ZERO, displayBlockState)

    override fun destroy(source: DamageSource) {
        this.kill()
        if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            val result: ItemStack = pickResult
            saveToDrop(result)
            if (this.hasCustomName()) {
                result.set(DataComponents.CUSTOM_NAME, this.customName)
            }
            this.spawnAtLocation(result)
            blockEntity.onRemove(this.level(), this.position())
        }
    }

    protected fun saveToDrop(stack: ItemStack) {
        this.blockEntity.saveToItem(stack, this.level().registryAccess())
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult =
        when (val result: InteractionResult = super.interact(player, hand)) {
            InteractionResult.SUCCESS -> result
            else -> extraInteract(player, hand)
        }

    protected open fun extraInteract(player: Player, hand: InteractionHand): InteractionResult = InteractionResult.PASS

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_BLOCK_ENTITY, CompoundTag())
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        saveBlockEntity().let(compound::merge)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        this.blockEntity = createBlockEntity()
        loadBlockEntity(compound)
        this.entityData.set(DATA_BLOCK_ENTITY, saveBlockEntity())
    }

    override fun onSyncedDataUpdated(key: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(key)
        if (DATA_BLOCK_ENTITY == key) {
            loadBlockEntity(this.entityData.get(DATA_BLOCK_ENTITY))
        }
    }

    private fun saveBlockEntity(): CompoundTag = this.blockEntity.saveWithoutMetadata(this.level().registryAccess())

    private fun loadBlockEntity(compound: CompoundTag) {
        this.blockEntity.loadCustomOnly(compound, this.level().registryAccess())
    }

    //    AbstractMinecart    //

    final override fun getMinecartType(): Type = Type.CHEST

    final override fun getDropItem(): Item = Items.MINECART

    abstract override fun getPickResult(): ItemStack

    abstract override fun getDefaultDisplayBlockState(): BlockState

    //    HTHandlerProvider    //

    final override fun getItemHandler(direction: Direction?): IItemHandler? = blockEntity.getItemHandler(direction)

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? = blockEntity.getFluidHandler(direction)

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = blockEntity.getEnergyStorage(direction)

    //    Factory    //

    fun interface Factory {
        fun create(
            level: Level,
            x: Double,
            y: Double,
            z: Double,
        ): HTMinecart<*>
    }
}
