package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.Identifier

//    NbtCompound    //

fun buildNbt(builderAction: NbtCompound.() -> Unit): NbtCompound = NbtCompound().apply(builderAction)

fun NbtCompound.putIdentifier(key: String, value: Identifier) {
    putString(key, value.toString())
}

fun NbtCompound.putMachineKey(key: String, value: HTMachineKey) {
    putIdentifier(key, value.id)
}

fun NbtCompound.putTier(key: String, value: HTMachineTier) {
    putString(key, value.asString())
}

fun NbtCompound.getIdentifier(key: String): Identifier = Identifier.of(getString(key))

fun NbtCompound.getMachineKey(key: String): HTMachineKey = HTMachineKey.of(getIdentifier(key))

fun NbtCompound.getTier(key: String): HTMachineTier =
    HTMachineTier.entries.firstOrNull { it.asString() == getString(key) } ?: HTMachineTier.PRIMITIVE

//    NbtList    //

fun buildNbtList(builderAction: NbtList.() -> Unit): NbtList = NbtList().apply(builderAction)
