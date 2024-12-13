package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier

//    NbtCompound    //

/**
 * Build new [NbtCompound] instance with [builderAction]
 */
fun buildNbt(builderAction: NbtCompound.() -> Unit): NbtCompound = NbtCompound().apply(builderAction)

//    NbtList    //

/**
 * Build new [NbtList] instance with [builderAction]
 */
fun buildNbtList(builderAction: NbtList.() -> Unit): NbtList = NbtList().apply(builderAction)

//    Writing    //

fun NbtCompound.putIdentifier(key: String, value: Identifier) {
    putString(key, value.toString())
}

fun NbtCompound.putMachineKey(key: String, value: HTMachineKey) {
    putIdentifier(key, value.id)
}

fun NbtCompound.putTier(key: String, value: HTMachineTier) {
    putString(key, value.asString())
}

fun Inventory.writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), iterateStacks())
        .ifSuccess { nbt.put("Inventory", it) }
        .ifError { nbt.put("Inventory", NbtList()) }
}

fun NbtCompound.writeItemStorage(key: String, storage: SingleItemStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    put(key, buildNbt { storage.writeNbt(this, wrapperLookup) })
}

fun NbtCompound.writeFluidStorage(key: String, storage: SingleFluidStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    put(key, buildNbt { storage.writeNbt(this, wrapperLookup) })
}

//    Reading    //

fun NbtCompound.getIdentifier(key: String): Identifier = Identifier.of(getString(key))

fun NbtCompound.getMachineKey(key: String): HTMachineKey = HTMachineKey.of(getIdentifier(key))

fun NbtCompound.getTier(key: String): HTMachineTier =
    HTMachineTier.entries.firstOrNull { it.asString() == getString(key) } ?: HTMachineTier.PRIMITIVE

fun Inventory.readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.get("Inventory"))
        .ifSuccess { it.forEachIndexed(::setStack) }
}

fun NbtCompound.readItemStorage(key: String, storage: SingleItemStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    storage.readNbt(getCompound(key), wrapperLookup)
}

fun NbtCompound.readFluidStorage(key: String, storage: SingleFluidStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    storage.readNbt(getCompound(key), wrapperLookup)
}
