package hiiragi283.ragium.common.item

import hiiragi283.core.api.HTDataSerializable
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.items.ItemStackHandler

class HTUniversalChestManager private constructor(private val map: MutableMap<DyeColor, ItemStackHandler>) : HTDataSerializable {
    companion object {
        @JvmStatic
        fun getHandler(server: MinecraftServer, color: DyeColor): ItemStackHandler =
            server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_CHEST).getHandler(color)
    }

    constructor() : this(mutableMapOf())

    fun getHandler(color: DyeColor): ItemStackHandler = map.computeIfAbsent(color) { ItemStackHandler(27) }

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (color: DyeColor in DyeColor.entries) {
            nbt.put(color.serializedName, getHandler(color).serializeNBT(provider))
        }
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (color: DyeColor in DyeColor.entries) {
            val tagIn: CompoundTag = nbt.getCompound(color.serializedName)
            if (tagIn.isEmpty) continue
            getHandler(color).deserializeNBT(provider, tagIn)
        }
    }
}
