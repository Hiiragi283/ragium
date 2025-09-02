package hiiragi283.ragium.common.storage

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler
import org.slf4j.Logger

/**
 * @see [mekanism.common.attachments.containers.ContainerType]
 */
class HTCapabilityCodec<CONTAINER : INBTSerializable<CompoundTag>>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot> = HTCapabilityCodec(
            RagiumConst.ITEMS,
            RagiumConst.SLOT,
            HTBlockEntity::getItemSlots,
            HTBlockEntity::hasItemHandler,
        )

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank> = HTCapabilityCodec(
            RagiumConst.FLUIDS,
            RagiumConst.TANK,
            HTBlockEntity::getFluidTanks,
            HTBlockEntity::hasFluidHandler,
        )

        @JvmField
        val TYPES: List<HTCapabilityCodec<*>> = listOf(ITEM, FLUID)

        @JvmStatic
        fun registerItem(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IItemHandler?, vararg items: ItemLike) {
            event.registerItem(
                HTMultiCapability.ITEM.itemCapability,
                { stack: ItemStack, _: Void? -> getter(stack) },
                *items,
            )
        }

        @JvmStatic
        fun registerFluid(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IFluidHandlerItem?, vararg items: ItemLike) {
            event.registerItem(
                HTMultiCapability.FLUID.itemCapability,
                { stack: ItemStack, _: Void? -> getter(stack) },
                *items,
            )
        }

        @JvmStatic
        fun registerEnergy(event: RegisterCapabilitiesEvent, getter: (ItemStack) -> IEnergyStorage?, vararg items: ItemLike) {
            event.registerItem(
                HTMultiCapability.ENERGY.itemCapability,
                { stack: ItemStack, _: Void? -> getter(stack) },
                *items,
            )
        }
    }

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
        for (slot: Int in containers.indices) {
            val nbt: CompoundTag = containers[slot].serializeNBT(provider)
            if (nbt.isEmpty) continue
            nbt.putInt(containerKey, slot)
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
        if (list.isEmpty()) return
        for (i: Int in list.indices) {
            val nbt: CompoundTag = list.getCompound(i)
            if (nbt.isEmpty) continue
            val slot: Int = nbt.getInt(containerKey)
            if (slot in (0 until containers.size)) {
                containers[slot].deserializeNBT(provider, nbt)
            }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
