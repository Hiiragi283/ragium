package hiiragi283.ragium.client.data

import hiiragi283.ragium.common.Ragium
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object RagiumModels {

    @JvmField
    val HULL = Model(
        Optional.of(Ragium.id("block/hull")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
    )

    @JvmField
    val MACHINE = Model(
        Optional.of(Ragium.id("block/machine")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
        TextureKey.FRONT
    )

}