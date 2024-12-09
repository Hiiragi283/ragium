package hiiragi283.ragium.data

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

//   VariantsBlockStateSupplier    //

fun buildVariantState(block: Block, action: MutableList<BlockStateVariant>.() -> Unit): VariantsBlockStateSupplier {
    val variants: List<BlockStateVariant> = buildList(action)
    return when (variants.size) {
        0 -> VariantsBlockStateSupplier.create(block)
        1 -> VariantsBlockStateSupplier.create(block, variants[0])
        else -> VariantsBlockStateSupplier.create(block, *variants.toTypedArray())
    }
}

//   MultipartBlockStateSupplier    //

fun buildMultipartState(block: Block, action: MultipartBlockStateSupplier.() -> Unit): BlockStateSupplier =
    MultipartBlockStateSupplier.create(block).apply(action)

fun <T : Comparable<T>> buildWhen(property: Property<T>, value: T): When.PropertyCondition = When.create().set(property, value)

fun buildWhen(action: When.PropertyCondition.() -> Unit): When = When.create().apply(action)

//    BlockStateVariant    //

fun stateVariantOf(action: BlockStateVariant.() -> Unit): BlockStateVariant = BlockStateVariant.create().apply(action)

fun stateVariantOf(block: Block): BlockStateVariant = stateVariantOf {
    model(TextureMap.getId(block))
}

fun stateVariantOf(modelId: Identifier): BlockStateVariant = stateVariantOf {
    model(modelId)
}

fun stateVariantOf(namespace: String, path: String): BlockStateVariant = stateVariantOf {
    model(namespace, path)
}

fun stateVariantOf(path: String): BlockStateVariant = stateVariantOf {
    model(path)
}

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
