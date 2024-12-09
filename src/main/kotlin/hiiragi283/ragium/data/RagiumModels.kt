package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.util.Identifier
import java.util.*

object RagiumModels {
    //    Block    //

    @JvmField
    val ALL_TINTED: Model = model(
        "block/all_tinted",
        TextureKey.ALL,
    )

    @JvmField
    val CROSS_TINTED: Model = model(
        "block/cross_tinted",
        TextureKey.CROSS,
    )

    @JvmField
    val CROSS_PIPE: Model =
        model(
            "block/cross_pipe",
            TextureKey.ALL,
        )

    @JvmField
    val EXPORTER: Model = model(
        "block/exporter",
        TextureKey.TOP,
        TextureKey.SIDE,
    )

    @JvmField
    val DISPLAY: Model = model(
        "block/display",
        TextureKey.TOP,
        TextureKey.SIDE,
    )

    @JvmField
    val PIPE: Model =
        model(
            "block/pipe",
            TextureKey.ALL,
        )

    @JvmField
    val PIPE_SIDE: Model =
        model(
            "block/pipe_side",
            TextureKey.ALL,
        )

    @JvmField
    val HULL: Model =
        model(
            "block/hull",
            TextureKey.TOP,
            TextureKey.SIDE,
            TextureKey.INSIDE,
        )

    @JvmField
    val LAYERED: Model =
        model(
            "block/layered",
            TextureKey.LAYER0,
            TextureKey.LAYER1,
        )

    @JvmField
    val SURFACE: Model =
        model(
            "block/surface",
            TextureKey.TOP,
        )

    //    Item    //

    @JvmField
    val FILLED_FLUID_CUBE: Model =
        model("item/fluid_cube")

    @JvmStatic
    fun model(path: String, vararg keys: TextureKey): Model = model(RagiumAPI.id(path), *keys)

    @JvmStatic
    fun model(id: Identifier, vararg keys: TextureKey): Model = Model(
        Optional.of(id),
        Optional.empty(),
        *keys,
    )
}
