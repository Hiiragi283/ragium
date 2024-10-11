package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.machineTier
import net.minecraft.block.Block
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.TexturedModel
import net.minecraft.util.Identifier
import java.util.*

object RagiumModels {
    @JvmField
    val MACHINE_MODEL_ID: Identifier = RagiumAPI.id("block/dynamic_machine")

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
    val DISPLAY: Model = model(
        "block/display",
        TextureKey.TOP,
        TextureKey.SIDE,
    )

    @JvmField
    val EMPTY: Model = model("block/empty")

    @JvmField
    val HULL: Model =
        model(
            "block/hull",
            TextureKey.TOP,
            TextureKey.BOTTOM,
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
    val DYNAMIC_MACHINE: Model = model(MACHINE_MODEL_ID.path)

    //    Item    //

    @JvmField
    val CLUSTER_ITEM: Model = model("item/cluster", TextureKey.LAYER0)

    @JvmField
    val FILLED_FLUID_CUBE: Model =
        model("item/filled_fluid_cube")

    @JvmStatic
    private fun model(path: String, vararg keys: TextureKey): Model = Model(
        Optional.of(RagiumAPI.id(path)),
        Optional.empty(),
        *keys,
    )

    //    Factory    //

    @JvmField
    val EMPTY_FACTORY: TexturedModel.Factory = TexturedModel.makeFactory({ TextureMap() }, EMPTY)

    @JvmField
    val HULL_TEXTURE_FACTORY: TexturedModel.Factory =
        TexturedModel.makeFactory({ block: Block ->
            val tier: HTMachineTier = block.asItem().components.machineTier
            textureMap {
                put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                put(
                    TextureKey.BOTTOM,
                    when (tier) {
                        HTMachineTier.PRIMITIVE -> Identifier.of("block/bricks")
                        HTMachineTier.BASIC -> Identifier.of("block/blast_furnace_top")
                        HTMachineTier.ADVANCED -> RagiumAPI.id("block/advanced_casing")
                    },
                )
            }
        }, HULL)

    @JvmStatic
    fun createAllTinted(all: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap.of(TextureKey.ALL, all)
    }, ALL_TINTED)

    @JvmStatic
    fun createCluster(layer0: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap.of(TextureKey.LAYER0, layer0)
    }, CLUSTER_ITEM)

    @JvmStatic
    fun createCrossTinted(cross: Identifier): TexturedModel.Factory = TexturedModel.makeFactory({
        TextureMap.of(TextureKey.CROSS, cross)
    }, CROSS_TINTED)

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
