package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.attachments.HTAttachedContainers
import hiiragi283.ragium.api.storage.attachments.HTAttachedEnergy
import hiiragi283.ragium.api.storage.attachments.HTAttachedFluids
import hiiragi283.ragium.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.common.attachments.containers.ContainerType
 */
class HTCapabilityCodec<CONTAINER : HTValueSerializable, ATTACHED : HTAttachedContainers<*, ATTACHED>>(
    private val component: DataComponentType<ATTACHED>,
    private val attachedFactory: (Int) -> ATTACHED,
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
    private val copyTo: (HTBlockEntity, List<CONTAINER>, ATTACHED) -> Unit,
    private val copyFrom: (HTBlockEntity, List<CONTAINER>) -> ATTACHED?,
) {
    companion object {
        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot, HTAttachedItems> = HTCapabilityCodec(
            RagiumDataComponents.ITEM,
            HTAttachedItems::create,
            RagiumConst.ITEMS,
            RagiumConst.SLOT,
            HTBlockEntity::getItemSlots,
            HTBlockEntity::hasItemHandler,
            HTBlockEntity::applyItemSlots,
            HTBlockEntity::collectItemSlots,
        )

        @JvmField
        val ENERGY: HTCapabilityCodec<HTEnergyBattery, HTAttachedEnergy> = HTCapabilityCodec(
            RagiumDataComponents.ENERGY,
            HTAttachedEnergy::create,
            RagiumConst.BATTERIES,
            RagiumConst.SLOT,
            HTBlockEntity::getEnergyBattery.andThen(::listOfNotNull),
            HTBlockEntity::hasEnergyStorage,
            HTBlockEntity::applyEnergyBattery,
            HTBlockEntity::collectEnergyBattery,
        )

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank, HTAttachedFluids> = HTCapabilityCodec(
            RagiumDataComponents.FLUID,
            HTAttachedFluids::create,
            RagiumConst.FLUIDS,
            RagiumConst.TANK,
            HTBlockEntity::getFluidTanks,
            HTBlockEntity::hasFluidHandler,
            HTBlockEntity::applyFluidTanks,
            HTBlockEntity::collectFluidTanks,
        )

        @JvmField
        val TYPES: List<HTCapabilityCodec<*, *>> = listOf(ITEM, ENERGY, FLUID)
    }

    //    Component    //

    fun getOrCreate(stack: ItemStack, size: Int): ATTACHED = stack.getOrDefault(component, attachedFactory(size))

    fun updateAttached(stack: ItemStack, attached: ATTACHED) {
        if (attached.isEmpty()) {
            stack.remove(component)
        } else {
            stack.set(component, attached)
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
            if (output.isEmpty()) {
                list.discardLast()
                return@forEachIndexed
            }
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

    /**
     * @see mekanism.common.attachments.containers.ContainerType.copyToTile
     */
    fun copyTo(blockEntity: HTBlockEntity, getter: (DataComponentType<ATTACHED>) -> ATTACHED?) {
        val component: ATTACHED = getter(this.component) ?: return
        copyTo(blockEntity, getContainers(blockEntity), component)
    }

    /**
     * @see mekanism.common.attachments.containers.ContainerType.copyFromTile
     */
    fun copyFrom(blockEntity: HTBlockEntity, builder: DataComponentMap.Builder) {
        val containers: List<CONTAINER> = getContainers(blockEntity)
        if (!containers.isEmpty()) {
            val component: ATTACHED = copyFrom(blockEntity, containers) ?: return
            builder.set(this.component, component)
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
