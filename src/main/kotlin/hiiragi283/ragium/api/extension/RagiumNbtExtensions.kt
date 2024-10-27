package hiiragi283.ragium.api.extension

import net.minecraft.nbt.NbtCompound

//    NbtCompound    //

fun buildNbt(builderAction: NbtCompound.() -> Unit): NbtCompound = NbtCompound().apply(builderAction)
