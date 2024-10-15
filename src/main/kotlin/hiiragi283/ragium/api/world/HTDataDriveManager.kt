package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState

class HTDataDriveManager :
    PersistentState(),
    MutableList<Identifier> by mutableListOf() {
    companion object {
        const val KEY = "data_drives"

        @JvmField
        val ID: Identifier = RagiumAPI.id(KEY)

        @JvmField
        val TYPE: Type<HTDataDriveManager> = Type(::HTDataDriveManager, Companion::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTDataDriveManager = Identifier.CODEC
            .listOf()
            .parse(NbtOps.INSTANCE, nbt.getList(KEY, NbtElement.STRING_TYPE.toInt()))
            .result()
            .map(Companion::fromCopy)
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
            }
        return nbt
    }
}
