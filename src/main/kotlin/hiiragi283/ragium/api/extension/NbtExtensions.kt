package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper

//    NbtCompound    //

/**
 * [NbtCompound]を返します。
 * @param builderAction [NbtCompound]を初期化するブロック
 */
fun buildNbt(builderAction: NbtCompound.() -> Unit): NbtCompound = NbtCompound().apply(builderAction)

//    NbtList    //

/**
 * [NbtList]を返します。
 * @param builderAction [NbtList]を初期化するブロック
 */
fun buildNbtList(builderAction: NbtList.() -> Unit): NbtList = NbtList().apply(builderAction)

//    Writing    //

/**
 * インベントリを[nbt]に書き込みます。
 *
 * エンコードに失敗した場合は空の[NbtList]が書き込まれます。
 */
fun Inventory.writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), iterateStacks())
        .ifSuccess { nbt.put("Inventory", it) }
        .ifError { nbt.put("Inventory", NbtList()) }
}

/**
 * [storage]をNBTに書き込みます。
 */
fun NbtCompound.writeItemStorage(key: String, storage: SingleItemStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    put(key, buildNbt { storage.writeNbt(this, wrapperLookup) })
}

/**
 * [storage]をNBTに書き込みます。
 */
fun NbtCompound.writeFluidStorage(key: String, storage: SingleFluidStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    put(key, buildNbt { storage.writeNbt(this, wrapperLookup) })
}

//    Reading    //

/**
 * [nbt]からインベントリを読み取ります。
 */
fun Inventory.readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.get("Inventory"))
        .ifSuccess { it.forEachIndexed(::setStack) }
}

/**
 * [storage]を[NbtCompound]から読み取ります。
 */
fun NbtCompound.readItemStorage(key: String, storage: SingleItemStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    storage.readNbt(getCompound(key), wrapperLookup)
}

/**
 * [storage]を[NbtCompound]から読み取ります。
 */
fun NbtCompound.readFluidStorage(key: String, storage: SingleFluidStorage, wrapperLookup: RegistryWrapper.WrapperLookup) {
    storage.readNbt(getCompound(key), wrapperLookup)
}
