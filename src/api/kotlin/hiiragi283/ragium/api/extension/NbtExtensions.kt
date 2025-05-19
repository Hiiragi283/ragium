package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps

//    CompoundTag    //

inline fun buildNbt(builderAction: CompoundTag.() -> Unit): CompoundTag = CompoundTag().apply(builderAction)

fun <T : Any> CompoundTag.putData(
    key: String,
    value: T,
    codec: Codec<T>,
    registryOps: RegistryOps<Tag>,
) {
    codec.encodeStart(registryOps, value).ifSuccess { put(key, it) }
}

fun <T : Any> CompoundTag.getData(key: String, codec: Codec<T>, registryOps: RegistryOps<Tag>): DataResult<T> =
    codec.parse(registryOps, get(key))
