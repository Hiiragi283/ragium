package hiiragi283.ragium.api.world

import com.mojang.serialization.Codec
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.saveddata.SavedData
import java.util.function.Supplier

@ConsistentCopyVisibility
data class HTSavedDataType<T : SavedData> private constructor(val id: ResourceLocation, val factory: SavedData.Factory<T>) {
    companion object {
        @JvmStatic
        fun <T : SavedData> create(id: ResourceLocation, codec: Codec<T>, factory: Supplier<T>): HTSavedDataType<T> = HTSavedDataType(
            id,
            SavedData.Factory(factory) { tag: CompoundTag, lookup: HolderLookup.Provider ->
                codec.parse(lookup.createSerializationContext(NbtOps.INSTANCE), tag).result().orElseGet(factory)
            },
        )
    }

    @JvmField
    val saveId: String = id.toDebugFileName()
}
