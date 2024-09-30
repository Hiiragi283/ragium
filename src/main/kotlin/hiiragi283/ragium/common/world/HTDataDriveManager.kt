package hiiragi283.ragium.common.world

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.util.getState
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import net.minecraft.world.World

val MinecraftServer.dataDriveManager: HTDataDriveManager
    get() = getState(overworld, HTDataDriveManager.TYPE, HTDataDriveManager.ID)

val ServerWorld.dataDriveManager: HTDataDriveManager
    get() = server.dataDriveManager

val World.dataDriveManager: HTDataDriveManager?
    get() = server?.dataDriveManager

class HTDataDriveManager :
    PersistentState(),
    MutableList<Identifier> by mutableListOf() {
    companion object {
        const val KEY = "data_drives"

        @JvmField
        val ID: Identifier = Ragium.id(KEY)

        @JvmField
        val TYPE: Type<HTDataDriveManager> = Type(::HTDataDriveManager, ::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTDataDriveManager = Identifier.CODEC
            .listOf()
            .parse(NbtOps.INSTANCE, nbt.getList(KEY, NbtElement.STRING_TYPE.toInt()))
            .result()
            .map(::fromCopy)
            .orElseGet(::HTDataDriveManager)

        @JvmStatic
        private fun fromCopy(copy: List<Identifier>): HTDataDriveManager = HTDataDriveManager().apply {
            copy.forEach(this::add)
        }
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        Identifier.CODEC
            .listOf()
            .encodeStart(NbtOps.INSTANCE, this)
            .result()
            .ifPresent {
                nbt.put(KEY, it)
                Ragium.log { info("Saved HTDataDriveManager!") }
            }
        return nbt
    }
}
