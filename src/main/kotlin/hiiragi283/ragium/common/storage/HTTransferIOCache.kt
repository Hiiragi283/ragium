package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.HTTransferIO
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.neoforged.neoforge.common.util.INBTSerializable

class HTTransferIOCache :
    HTTransferIO.Provider,
    HTTransferIO.Receiver,
    INBTSerializable<CompoundTag> {
    private val cache: MutableMap<Direction, HTTransferIO> = mutableMapOf()

    override fun apply(direction: Direction): HTTransferIO = cache[direction] ?: HTTransferIO.BOTH

    override fun accept(direction: Direction, transferIO: HTTransferIO) {
        cache[direction] = transferIO
    }

    //    INBTSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        for (direction: Direction in Direction.entries) {
            val transferIO: HTTransferIO = cache[direction] ?: continue
            HTTransferIO.CODEC
                .encodeStart(NbtOps.INSTANCE, transferIO)
                .ifSuccess { this.put(direction.serializedName, it) }
        }
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (direction: Direction in Direction.entries) {
            HTTransferIO.CODEC
                .parse(NbtOps.INSTANCE, nbt.get(direction.serializedName))
                .ifSuccess { set(direction, it) }
        }
    }
}
