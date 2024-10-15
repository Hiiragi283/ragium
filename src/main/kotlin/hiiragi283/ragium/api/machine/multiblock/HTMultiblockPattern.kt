package hiiragi283.ragium.api.machine.multiblock

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.mappedCodecOf
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.BlockPos

class HTMultiblockPattern(private val map: Map<BlockPos, HTMultiblockComponent>) {
    companion object {
        @JvmField
        val REGISTRY_KEY: RegistryKey<Registry<HTMultiblockPattern>> =
            RegistryKey.ofRegistry(RagiumAPI.id("multiblock"))

        @JvmField
        val MAP_CODEC: Codec<Map<BlockPos, HTMultiblockComponent>> = mappedCodecOf(
            BlockPos.CODEC.fieldOf("pos"),
            HTMultiblockComponent.CODEC.fieldOf("blocks"),
        )

        @JvmField
        val CODEC: Codec<HTMultiblockPattern> = MAP_CODEC.xmap(::HTMultiblockPattern, HTMultiblockPattern::map)
    }

    fun buildMultiblock(builder: HTMultiblockBuilder) {
        map.forEach { (pos: BlockPos, component: HTMultiblockComponent) ->
            builder.add(pos.x, pos.y, pos.z, component)
        }
    }
}
