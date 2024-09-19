package hiiragi283.ragium.common.util

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier

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

fun buildStateVariant(action: BlockStateVariant.() -> Unit): BlockStateVariant = BlockStateVariant.create().apply(action)

fun buildModelVariant(modelId: Identifier): BlockStateVariant = buildStateVariant {
    model(modelId)
}

fun buildModelVariant(namespace: String, path: String): BlockStateVariant = buildStateVariant {
    model(namespace, path)
}

fun buildModelVariant(path: String): BlockStateVariant = buildStateVariant {
    model(path)
}

fun BlockStateVariant.rotX(rot: VariantSettings.Rotation): BlockStateVariant = put(VariantSettings.X, rot)

fun BlockStateVariant.rotY(rot: VariantSettings.Rotation): BlockStateVariant = put(VariantSettings.Y, rot)

fun BlockStateVariant.model(modelId: Identifier): BlockStateVariant = put(VariantSettings.MODEL, modelId)

fun BlockStateVariant.model(namespace: String, path: String): BlockStateVariant = model(Identifier.of(namespace, path))

fun BlockStateVariant.model(path: String): BlockStateVariant = model(Identifier.ofVanilla(path))
