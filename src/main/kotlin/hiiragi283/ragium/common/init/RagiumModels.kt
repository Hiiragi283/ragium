package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.Block
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.TexturedModel
import net.minecraft.util.Identifier
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

    @JvmField
    val EMPTY: Model = model("block/empty")

    @JvmField
    val DISPLAY: Model = model(
        "block/display",
        TextureKey.TOP,
        TextureKey.SIDE,
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

    @JvmStatic
    fun createLayered(inner: Identifier, outer: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap()
            .put(TextureKey.LAYER0, inner)
            .put(TextureKey.LAYER1, outer)
    }, LAYERED)

    @JvmField
    val HULL_TEXTURE_FACTORY: TexturedModel.Factory =
        TexturedModel.makeFactory({ block: Block ->
            val tier: HTMachineTier =
                block.asItem().components.getOrDefault(RagiumComponentTypes.TIER, HTMachineTier.HEAT)
            TextureMap()
                .put(TextureKey.TOP, tier.casingTex)
                .put(TextureKey.BOTTOM, tier.baseTex)
        }, HULL)

    @JvmStatic
    fun createMachine(top: Identifier, bottom: Identifier, front: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap().put(TextureKey.TOP, top).put(TextureKey.BOTTOM, bottom).put(TextureKey.FRONT, front)
    }, MACHINE)

    @JvmStatic
    fun createMachine(top: Block, bottom: Block, front: Identifier): TexturedModel.Factory =
        createMachine(TextureMap.getId(top), TextureMap.getId(bottom), front)

    @JvmField
    val EMPTY_FACTORY: TexturedModel.Factory = TexturedModel.makeFactory({ TextureMap() }, EMPTY)

    @JvmStatic
    fun createDisplay(top: Identifier, side: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap().put(TextureKey.TOP, top).put(TextureKey.SIDE, side)
    }, DISPLAY)
}
