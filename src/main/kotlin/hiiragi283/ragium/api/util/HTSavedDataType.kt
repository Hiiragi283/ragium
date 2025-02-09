package hiiragi283.ragium.api.util

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.saveddata.SavedData
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

data class HTSavedDataType<T : SavedData>(val id: ResourceLocation, val factory: SavedData.Factory<T>) {
    constructor(
        id: ResourceLocation,
        factory: Supplier<T>,
        deserializer: Function<CompoundTag, T>,
    ) : this(
        id,
        SavedData.Factory(
            factory,
            BiFunction { tag: CompoundTag, _: HolderLookup.Provider -> deserializer.apply(tag) },
            null,
        ),
    )

    constructor(
        id: ResourceLocation,
        factory: Supplier<T>,
        deserializer: BiFunction<CompoundTag, HolderLookup.Provider, T>,
    ) : this(id, SavedData.Factory(factory, deserializer, null))

    val saveId: String = id.namespace + '_' + id.path
}
