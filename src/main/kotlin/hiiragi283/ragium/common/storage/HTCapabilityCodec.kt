package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [mekanism.common.attachments.containers.ContainerType]
 */
class HTCapabilityCodec<CONTAINER : HTValueSerializable>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
) {
    companion object {
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

    fun saveTo(output: HTValueOutput, blockEntity: HTBlockEntity) {
        saveTo(output, getContainers(blockEntity))
    }

    fun saveTo(output: HTValueOutput, containers: List<CONTAINER>) {
        save(output.childrenList(containerTag), containers)
    }

    private fun save(list: HTValueOutput.ValueOutputList, containers: List<CONTAINER>) {
        containers.forEachIndexed { slot: Int, container: CONTAINER ->
            val output: HTValueOutput = list.addChild()
            container.serialize(output)
            output.putInt(containerKey, slot)
        }
    }

    fun loadFrom(input: HTValueInput, blockEntity: HTBlockEntity) {
        loadFrom(input, getContainers(blockEntity))
    }

    fun loadFrom(input: HTValueInput, containers: List<CONTAINER>) {
        load(input.childrenListOrEmpty(containerTag), containers)
    }

    private fun load(list: HTValueInput.ValueInputList, containers: List<CONTAINER>) {
        if (list.isEmpty) return
        for (input: HTValueInput in list) {
            val slot: Int = input.getInt(containerKey) ?: continue
            if (slot in containers.indices) {
                containers[slot].deserialize(input)
            }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
