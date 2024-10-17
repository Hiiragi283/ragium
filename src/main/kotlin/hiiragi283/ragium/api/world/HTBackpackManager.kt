package hiiragi283.ragium.api.world

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.mappedCodecOf
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState

class HTBackpackManager : PersistentState() {
    companion object {
        const val KEY = "backpack"

        @JvmField
        val ID: Identifier = RagiumAPI.id(KEY)

        @JvmField
        val CODEC: Codec<Map<DyeColor, HTSimpleInventory>> = mappedCodecOf(
            DyeColor.CODEC.fieldOf("color"),
            HTSimpleInventory.CODEC.fieldOf("inventory"),
        )

        @JvmField
        val TYPE: Type<HTBackpackManager> = Type(::HTBackpackManager, Companion::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTBackpackManager {
            val manager = HTBackpackManager()
            CODEC
                .parse(NbtOps.INSTANCE, nbt.get(KEY))
                .result()
                .ifPresent { map: Map<DyeColor, HTSimpleInventory> ->
                    map.forEach { (color: DyeColor, inventory: HTSimpleInventory) ->
                        manager.backpacks[color] = inventory
                    }
                }
            return manager
        }
    }

    private val backpacks: MutableMap<DyeColor, HTSimpleInventory> = mutableMapOf()

    operator fun get(color: DyeColor): HTSimpleInventory = backpacks.computeIfAbsent(color) { HTSimpleInventory(56) }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound = nbt.apply {
        CODEC
            .encodeStart(NbtOps.INSTANCE, backpacks)
            .result()
            .ifPresent { nbt.put(KEY, it) }
    }
}
