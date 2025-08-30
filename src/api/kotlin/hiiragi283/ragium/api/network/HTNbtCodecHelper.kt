package hiiragi283.ragium.api.network

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.INBTSerializable

object HTNbtCodecHelper {
    @JvmStatic
    fun slotSerializer(slots: List<HTItemSlot>): INBTSerializable<CompoundTag> = object : INBTSerializable<CompoundTag> {
        override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
            val list = ListTag()
            slots.forEachIndexed { index: Int, slot: HTItemSlot ->
                if (!slot.isEmpty) {
                    val nbt: CompoundTag = slot.serializeNBT(provider)
                    nbt.putInt("Slot", index)
                    list.add(nbt)
                }
            }
            return buildNbt { put("Items", list) }
        }

        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
            val list: ListTag = nbt.getList("Items", 10)
            for (tag: Tag in list) {
                if (tag is CompoundTag) {
                    val index: Int = tag.getInt("Slot")
                    ItemStack.parse(provider, tag).ifPresent { stack: ItemStack ->
                        slots.getOrNull(index)?.setStack(stack)
                    }
                }
            }
        }
    }
}
