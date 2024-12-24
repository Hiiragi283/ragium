package hiiragi283.ragium.data

import com.google.gson.JsonElement
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.function.BiConsumer
import java.util.function.Supplier

//   VariantsBlockStateSupplier    //

//   MultipartBlockStateSupplier    //

fun buildMultipartState(block: Block, action: MultipartBlockStateSupplier.() -> Unit): BlockStateSupplier =
    MultipartBlockStateSupplier.create(block).apply(action)

fun buildMultipartState(content: HTBlockContent, action: MultipartBlockStateSupplier.() -> Unit): BlockStateSupplier =
    MultipartBlockStateSupplier.create(content.get()).apply(action)

fun <T : Comparable<T>> buildWhen(property: Property<T>, value: T): When.PropertyCondition = When.create().set(property, value)

// fun buildWhen(action: When.PropertyCondition.() -> Unit): When = When.create().apply(action)

//    BlockStateVariant    //

fun stateVariantOf(content: HTBlockContent): BlockStateVariant = stateVariantOf(content.get())

fun stateVariantOf(block: Block): BlockStateVariant = stateVariantOf(TextureMap.getId(block))

fun stateVariantOf(modelId: Identifier): BlockStateVariant = BlockStateVariant.create().model(modelId)

fun BlockStateVariant.rotX(rot: VariantSettings.Rotation): BlockStateVariant = put(VariantSettings.X, rot)

fun BlockStateVariant.rotY(rot: VariantSettings.Rotation): BlockStateVariant = put(VariantSettings.Y, rot)

fun BlockStateVariant.model(modelId: Identifier): BlockStateVariant = put(VariantSettings.MODEL, modelId)

fun BlockStateVariant.model(namespace: String, path: String): BlockStateVariant = model(Identifier.of(namespace, path))

fun BlockStateVariant.model(path: String): BlockStateVariant = model(Identifier.ofVanilla(path))

fun BlockStateVariant.rot(direction: Direction): BlockStateVariant = when (direction) {
    Direction.DOWN -> rotX(VariantSettings.Rotation.R90)
    Direction.UP -> rotX(VariantSettings.Rotation.R270)
    Direction.NORTH -> this
    Direction.SOUTH -> rotY(VariantSettings.Rotation.R180)
    Direction.WEST -> rotY(VariantSettings.Rotation.R270)
    Direction.EAST -> rotY(VariantSettings.Rotation.R90)
}

//    Model    //

typealias ModelCollector = BiConsumer<Identifier, Supplier<JsonElement>>

fun Model.upload(content: HTBlockContent, textureMap: TextureMap, modelCollector: ModelCollector): Identifier =
    upload(content.get(), textureMap, modelCollector)

fun Model.upload(content: HTItemContent, textureMap: TextureMap, modelCollector: ModelCollector): Identifier =
    upload(content.id.withPrefixedPath("item/"), textureMap, modelCollector)

fun Model.uploadAsItem(block: Block, textureMap: TextureMap, modelCollector: ModelCollector): Identifier =
    upload(TextureMap.getId(block.asItem()), textureMap, modelCollector)
