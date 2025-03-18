package hiiragi283.ragium.api.extension

import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

//    JsonObject    //

fun buildNbt(builderAction: CompoundTag.() -> Unit): CompoundTag = CompoundTag().apply(builderAction)

fun CompoundTag.putId(name: String, value: ResourceLocation) {
    putString(name, value.toString())
}
