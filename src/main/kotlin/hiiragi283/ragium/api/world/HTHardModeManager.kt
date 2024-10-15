package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState

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
