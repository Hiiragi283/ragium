package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.extensions.ILevelExtension
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [mekanism.common.attachments.containers.ContainerType]
 */
class HTCapabilityType<CONTAINER : INBTSerializable<CompoundTag>, HANDLER : Any, ITEM_HANDLER : HANDLER>(
    val blockCapability: BlockCapability<HANDLER, Direction?>,
    val itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
) {
    companion object {
        @JvmField
        val ITEM: HTCapabilityType<HTItemSlot, IItemHandler, IItemHandler> = HTCapabilityType(
            Capabilities.ItemHandler.BLOCK,
            Capabilities.ItemHandler.ITEM,
            RagiumConst.ITEMS,
            RagiumConst.SLOT,
            HTBlockEntity::getItemSlots,
        )

        @JvmField
        val FLUID: HTCapabilityType<HTFluidTank, IFluidHandler, IFluidHandlerItem> = HTCapabilityType(
            Capabilities.FluidHandler.BLOCK,
            Capabilities.FluidHandler.ITEM,
            RagiumConst.FLUIDS,
            RagiumConst.TANK,
            HTBlockEntity::getFluidTanks,
        )

        @JvmStatic
        fun <BE : HTBlockEntity> register(event: RegisterCapabilitiesEvent, type: HTDeferredBlockEntityType<BE>) {
            event.registerBlockEntity(ITEM.blockCapability, type.get(), HTHandlerBlockEntity::getItemHandler)
            event.registerBlockEntity(FLUID.blockCapability, type.get(), HTHandlerBlockEntity::getFluidHandler)
        }

        @JvmStatic
        fun registerItem(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IItemHandler?, vararg items: ItemLike) {
            event.registerItem(ITEM.itemCapability, { stack: ItemStack, _: Void? -> getter(stack) }, *items)
        }

        @JvmStatic
        fun registerFluid(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IFluidHandlerItem?, vararg items: ItemLike) {
            event.registerItem(FLUID.itemCapability, { stack: ItemStack, _: Void? -> getter(stack) }, *items)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? =
        level.getCapability(blockCapability, pos, side)

    fun getCapability(stack: ItemStack): ITEM_HANDLER? = stack.getCapability(itemCapability)

    //    Save & Read    //

    fun saveTo(nbt: CompoundTag, provider: HolderLookup.Provider, blockEntity: HTBlockEntity) {
        saveTo(nbt, provider, getContainers(blockEntity))
    }

    fun saveTo(nbt: CompoundTag, provider: HolderLookup.Provider, containers: List<CONTAINER>) {
        val list: ListTag = save(provider, containers)
        if (!list.isEmpty()) {
            nbt.put(containerTag, list)
        }
    }

    private fun save(provider: HolderLookup.Provider, containers: List<CONTAINER>): ListTag {
        val list = ListTag()
        containers.forEachIndexed { index: Int, container: CONTAINER ->
            val nbt: CompoundTag = container.serializeNBT(provider)
            nbt.putInt(containerKey, index)
            list.add(nbt)
        }
        return list
    }

    fun readFrom(nbt: CompoundTag, provider: HolderLookup.Provider, blockEntity: HTBlockEntity) {
        readFrom(nbt, provider, getContainers(blockEntity))
    }

    fun readFrom(nbt: CompoundTag, provider: HolderLookup.Provider, containers: List<CONTAINER>) {
        read(provider, containers, nbt.getList(containerTag, Tag.TAG_COMPOUND.toInt()))
    }

    private fun read(provider: HolderLookup.Provider, containers: List<CONTAINER>, list: ListTag) {
        for (i: Int in list.indices) {
            val nbt: CompoundTag = list.getCompound(i)
            val slot: Int = nbt.getInt(containerKey)
            if (slot in containers.indices) {
                containers[slot].deserializeNBT(provider, nbt)
            }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)
}
