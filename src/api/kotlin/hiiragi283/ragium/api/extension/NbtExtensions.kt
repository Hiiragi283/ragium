package hiiragi283.ragium.api.extension

import net.minecraft.nbt.CompoundTag

//    CompoundTag    //

inline fun buildNbt(builderAction: CompoundTag.() -> Unit): CompoundTag = CompoundTag().apply(builderAction)
