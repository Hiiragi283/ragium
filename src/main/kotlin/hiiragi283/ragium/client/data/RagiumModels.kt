package hiiragi283.ragium.client.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.Block
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.TexturedModel
import java.util.*

object RagiumModels {
    //    Block    //

    @JvmField
    val LAYERED: Model =
        model(
            "block/layered",
            TextureKey.LAYER0,
            TextureKey.LAYER1,
        )

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
    val FILLED_FLUID_CUBE: Model =
        model("item/filled_fluid_cube")

    @JvmStatic
    private fun model(path: String, vararg keys: TextureKey): Model = Model(
        Optional.of(Ragium.id(path)),
        Optional.empty(),
        *keys,
    )

    //    Factory    //
    @JvmField
    val HULL_TEXTURE_FACTORY: TexturedModel.Factory =
        TexturedModel.makeFactory({ block: Block ->
            val tier: HTMachineTier =
                block.asItem().components.getOrDefault(RagiumComponentTypes.TIER, HTMachineTier.HEAT)
            TextureMap()
                .put(TextureKey.TOP, tier.casingTex)
                .put(TextureKey.BOTTOM, tier.baseTex)
        }, RagiumModels.HULL)
}
