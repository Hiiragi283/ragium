package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.util.HTSavedDataType
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler

class HTSculkItemStorage(private val handlerMap: Map<DyeColor, ItemStackHandler>) : SavedData() {
    companion object {
        const val KEY = "item_storage"

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTSculkItemStorage> =
            HTSavedDataType(RagiumAPI.id(KEY), ::HTSculkItemStorage, ::load)

        @JvmStatic
        private fun load(tag: CompoundTag, registries: HolderLookup.Provider): HTSculkItemStorage {
            val storage = HTSculkItemStorage()
            DyeColor.entries.associateWith { color: DyeColor ->
                val tagIn: CompoundTag = tag.getCompound(color.serializedName)
                storage.handlerMap[color]?.deserializeNBT(registries, tagIn)
            }
            return storage
        }
    }

    constructor() : this(DyeColor.entries.associateWith { ItemStackHandler(54) })

    fun getHandler(color: DyeColor): IItemHandlerModifiable = handlerMap[color] ?: EmptyItemHandler.INSTANCE as IItemHandlerModifiable

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag = buildNbt {
        for ((color: DyeColor, handler: ItemStackHandler) in handlerMap) {
            tag.put(color.serializedName, handler.serializeNBT(registries))
        }
    }
}
