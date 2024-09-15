package hiiragi283.ragium.client.data

import hiiragi283.ragium.common.Ragium
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object RagiumModels {
    //    Block    //

    @JvmField
    val HULL: Model =
        model(
            "block/hull",
            TextureKey.TOP,
            TextureKey.BOTTOM,
        )

    @JvmField
    val MACHINE: Model =
        model(
            "block/machine",
            TextureKey.TOP,
            TextureKey.BOTTOM,
            TextureKey.FRONT,
        )

    //    Item    //

    @JvmField
    val FLUID_CUBE: Model =
        model(
            "item/fluid_cube",
            TextureKey.ALL,
            TextureKey.INSIDE,
        )

    @JvmStatic
    private fun model(path: String, vararg keys: TextureKey): Model = Model(
        Optional.of(Ragium.id(path)),
        Optional.empty(),
        *keys,
    )
}
