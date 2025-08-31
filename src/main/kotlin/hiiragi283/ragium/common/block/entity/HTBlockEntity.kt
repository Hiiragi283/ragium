package hiiragi283.ragium.common.block.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.HTCapabilityType
import hiiragi283.ragium.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.ragium.common.storage.resolver.HTItemHandlerManager
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [mekanism.common.tile.base.TileEntityMekanism]
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(
        type,
        pos,
        state,
    ),
    Nameable,
    HTNbtCodec,
    HTItemHandler,
    HTFluidHandler,
    HTHandlerBlockEntity {
    //    Ticking    //

    companion object {
        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickClient]
         */
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

        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickServer]
         */
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

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Save & Read    //

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val writer: HTNbtCodec.Writer = object : HTNbtCodec.Writer {
            override fun <T : Any> write(codec: Codec<T>, key: String, value: T) {
                codec
                    .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                    .ifSuccess { tag.put(key, it) }
                    .ifError { error: DataResult.Error<Tag> -> LOGGER.error(error.message()) }
            }

            override fun write(key: String, serializable: INBTSerializable<CompoundTag>) {
                tag.put(key, serializable.serializeNBT(registries))
            }
        }
        // Custom Name
        writer.writeNullable(BiCodecs.TEXT, "custom_name", customName)
        // Capability
        HTCapabilityType.ITEM.saveTo(tag, registries, this)
        HTCapabilityType.FLUID.saveTo(tag, registries, this)
        // Custom
        writeNbt(writer)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val reader: HTNbtCodec.Reader = object : HTNbtCodec.Reader {
            override fun <T : Any> read(codec: Codec<T>, key: String): DataResult<T> = codec
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get(key))

            override fun read(key: String, serializable: INBTSerializable<CompoundTag>) {
                serializable.deserializeNBT(registries, tag.getCompound(key))
            }
        }
        // Custom Name
        reader.read(BiCodecs.TEXT, "custom_name").ifSuccess { customName = it }
        // Capability
        HTCapabilityType.ITEM.readFrom(tag, registries, this)
        HTCapabilityType.FLUID.readFrom(tag, registries, this)
        // Custom
        readNbt(reader)
    }

    //    Nameable    //

    private var customName: Component? = null

    final override fun getName(): Component = customName ?: blockState.block.name

    final override fun getCustomName(): Component? = customName

    //    Capability    //

    protected var itemHandlerManager: HTItemHandlerManager? = null
        private set
    protected var fluidHandlerManager: HTFluidHandlerManager? = null
        private set

    override fun afterLevelInit(level: Level) {
        super.afterLevelInit(level)
        itemHandlerManager = HTItemHandlerManager(initializeItemHandler(this), this)
        fluidHandlerManager = HTFluidHandlerManager(initializeFluidHandler(this), this)
    }

    // Item
    protected open fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? = null

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

    override fun dropInventory(consumer: (ItemStack) -> Unit) {
        super.dropInventory(consumer)
        getItemSlots(getItemSideFor()).map(HTItemSlot::getStack).forEach(consumer)
    }

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(HTMultiCapability.ITEM, direction)

    // Fluid
    protected open fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? = null

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? =
        fluidHandlerManager?.resolve(HTMultiCapability.FLUID, direction)
}
