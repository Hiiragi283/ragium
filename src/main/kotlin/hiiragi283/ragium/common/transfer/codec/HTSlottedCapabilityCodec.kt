package hiiragi283.ragium.common.transfer.codec

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * [HTBlockEntity]に対して[CONTAINER]の一覧を読み書きするクラスです。
 *
 * 参照 : [Mekanism - IContainerType](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/component/containers/type/IContainerType.java)
 * @param CONTAINER 保存の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTSlottedCapabilityCodec<CONTAINER : Any>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    canHandle: (HTBlockEntity) -> Boolean,
) : HTCapabilityCodec(canHandle) {
    companion object {
        @JvmField
        val ITEM: HTSlottedCapabilityCodec<HTItemSlot> =
            HTSlottedCapabilityCodec(
                HTConstants.ITEMS,
                HTConstants.SLOT,
                HTBlockEntity::getItemSlots,
                HTBlockEntity::hasItemHandler,
            )

        @JvmField
        val FLUID: HTSlottedCapabilityCodec<HTFluidTank> =
            HTSlottedCapabilityCodec(
                HTConstants.FLUIDS,
                HTConstants.TANK,
                HTBlockEntity::getFluidTanks,
                HTBlockEntity::hasFluidHandler,
            )
    }

    //    Save & Read    //

    override fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        saveTo(output, getContainers(blockEntity))
    }

    private fun saveTo(output: ValueOutput, containers: List<CONTAINER>) {
        save(output.childrenList(containerTag), containers)
    }

    private fun save(list: ValueOutput.ValueOutputList, containers: List<CONTAINER>) {
        for (slot: Int in containers.indices) {
            val container: CONTAINER = containers[slot]
            val output: ValueOutput = list.addChild()
            if (container is ValueIOSerializable) {
                container.serialize(output)
                if (output.isEmpty) {
                    list.discardLast()
                    continue
                }
                output.putInt(containerKey, slot)
            } else {
                RagiumAPI.LOGGER.warn("Container slot: $container does not support serialization")
            }
        }
    }

    override fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        loadFrom(input, getContainers(blockEntity))
    }

    private fun loadFrom(input: ValueInput, containers: List<CONTAINER>) {
        load(input.childrenListOrEmpty(containerTag), containers)
    }

    private fun load(list: Iterable<ValueInput>, containers: List<CONTAINER>) {
        if (list.none()) return
        for (input: ValueInput in list) {
            input.getInt(containerKey)
                .filter(containers.indices::contains)
                .map(containers::get)
                .ifPresent {
                    if (it is ValueIOSerializable) {
                        it.deserialize(input)
                    } else {
                        RagiumAPI.LOGGER.warn("Container slot: $it does not support deserialization")
                    }
                }
        }
    }

    private fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)
}
