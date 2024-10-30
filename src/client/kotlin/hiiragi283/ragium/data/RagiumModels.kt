package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.client.model.HTMachineModel
import net.minecraft.block.Block
import net.minecraft.data.client.*
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
    val EMPTY: Model = model("block/empty")

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
    val MACHINE: Model =
        model(
            "block/machine",
            TextureKey.TOP,
            TextureKey.BOTTOM,
            TextureKey.FRONT,
        )

    @JvmField
    val DYNAMIC_MACHINE: Model = model(HTMachineModel.MODEL_ID.path)

    //    Item    //

    @JvmField
    val CLUSTER_ITEM: Model = model("item/cluster", TextureKey.LAYER0)

    @JvmField
    val FILLED_FLUID_CUBE: Model =
        model("item/fluid_cube")

    @JvmStatic
    private fun model(path: String, vararg keys: TextureKey): Model = Model(
        Optional.of(RagiumAPI.id(path)),
        Optional.empty(),
        *keys,
    )

    //    Factory    //

    @JvmField
    val HULL_TEXTURE_FACTORY: TexturedModel.Factory =
        TexturedModel.makeFactory({ block: Block ->
            val tier: HTMachineTier = block.asItem().components.machineTier
            textureMap {
                put(
                    TextureKey.INSIDE,
                    when (tier) {
                        HTMachineTier.PRIMITIVE -> Identifier.of("block/bricks")
                        HTMachineTier.BASIC -> Identifier.of("block/blast_furnace_top")
                        HTMachineTier.ADVANCED -> RagiumAPI.id("block/advanced_casing")
                    },
                )
                put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                put(TextureKey.SIDE, ModelIds.getBlockModelId(block))
            }
        }, HULL)

    @JvmStatic
    fun createAllTinted(all: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap.of(TextureKey.ALL, all)
    }, ALL_TINTED)

    @JvmStatic
    fun createDisplay(top: Identifier, side: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        textureMap {
            put(TextureKey.TOP, top)
            put(TextureKey.SIDE, side)
        }
    }, DISPLAY)

    @JvmStatic
    fun createLayered(inner: Identifier, outer: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        textureMap {
            put(TextureKey.LAYER0, inner)
            put(TextureKey.LAYER1, outer)
        }
    }, LAYERED)

    @JvmStatic
    fun createMachine(top: Identifier, bottom: Identifier, front: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        textureMap {
            put(TextureKey.TOP, top)
            put(TextureKey.BOTTOM, bottom)
            put(TextureKey.FRONT, front)
        }
    }, MACHINE)

    @JvmStatic
    fun createMachine(top: Block, bottom: Block, front: Identifier): TexturedModel.Factory =
        createMachine(TextureMap.getId(top), TextureMap.getId(bottom), front)
}
