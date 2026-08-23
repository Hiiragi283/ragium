package hiiragi283.ragium.common.transfer

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * [HTBlockEntity]に対して[CONTAINER]の一覧を読み書きするクラスです。
 *
 * 参照 : [Mekanism - IContainerType](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/component/containers/type/IContainerType.java)
 * @param CONTAINER 保存の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTCapabilityCodec<CONTAINER : ValueIOSerializable>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
) {
    companion object {
        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot> = HTCapabilityCodec(HTConstants.ITEMS, HTConstants.SLOT, HTBlockEntity::getItemSlots, HTBlockEntity::hasItemHandler)

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank> = HTCapabilityCodec(HTConstants.FLUIDS, HTConstants.TANK, HTBlockEntity::getFluidTanks, HTBlockEntity::hasFluidHandler)

        @JvmField
        val TYPES: List<HTCapabilityCodec<*>> = listOf(ITEM, FLUID)
    }

    //    Save & Read    //

    fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        saveTo(output, getContainers(blockEntity))
    }

    fun saveTo(output: ValueOutput, containers: List<CONTAINER>) {
        save(output.childrenList(containerTag), containers)
    }

    private fun save(list: ValueOutput.ValueOutputList, containers: List<CONTAINER>) {
        for (slot: Int in containers.indices) {
            val container: CONTAINER = containers[slot]
            val output: ValueOutput = list.addChild()
            container.serialize(output)
            if (output.isEmpty) {
                list.discardLast()
                continue
            }
            output.putInt(containerKey, slot)
        }
    }

    fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        loadFrom(input, getContainers(blockEntity))
    }

    fun loadFrom(input: ValueInput, containers: List<CONTAINER>) {
        load(input.childrenListOrEmpty(containerTag), containers)
    }

    private fun load(list: Iterable<ValueInput>, containers: List<CONTAINER>) {
        if (list.none()) return
        for (input: ValueInput in list) {
            input.getInt(containerKey)
                .filter(containers.indices::contains)
                .map(containers::get)
                .ifPresent { it.deserialize(input) }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
