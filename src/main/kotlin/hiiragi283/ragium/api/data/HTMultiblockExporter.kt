package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.mappedCodecOf
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import net.minecraft.util.math.BlockPos

class HTMultiblockExporter : HTMultiblockBuilder {
    private val builder: MutableMap<BlockPos, HTMultiblockComponent> = mutableMapOf()

    companion object {
        @JvmField
        val CODEC: Codec<Map<BlockPos, HTMultiblockComponent>> = mappedCodecOf(
            BlockPos.CODEC.fieldOf("pos"),
            HTMultiblockComponent.CODEC.fieldOf("blocks"),
        )

        @JvmStatic
        fun create(builderAction: HTMultiblockExporter.() -> Unit): HTMultiblockPattern =
            HTMultiblockExporter().apply(builderAction).build()
    }

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        builder[BlockPos(x, y, z)] = component
    }

    fun build(): HTMultiblockPattern = HTMultiblockPattern(builder)
}
