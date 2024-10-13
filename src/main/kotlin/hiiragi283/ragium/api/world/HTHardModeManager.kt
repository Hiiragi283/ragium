package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState

val MinecraftServer.hardModeManager: HTHardModeManager
    get() = getState(overworld, HTHardModeManager.TYPE, HTHardModeManager.ID)

class HTHardModeManager() : PersistentState() {
    companion object {
        const val KEY = "is_hard_mode"

        @JvmField
        val ID: Identifier = RagiumAPI.id(KEY)

        @JvmField
        val TYPE: Type<HTHardModeManager> = Type(::HTHardModeManager, Companion::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTHardModeManager =
            HTHardModeManager(nbt.getBoolean(KEY))
    }

    var isHardMode: Boolean = false

    constructor(boolean: Boolean) : this() {
        isHardMode = boolean
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        nbt.putBoolean(KEY, isHardMode)
        return nbt
    }
}
