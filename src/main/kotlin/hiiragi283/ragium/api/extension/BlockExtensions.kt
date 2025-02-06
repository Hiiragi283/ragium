package hiiragi283.ragium.api.extension

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

//    Block    //

/**
 * [BlockBehaviour.Properties]の新しいインスタンスを返します。
 */
fun blockProperty(): BlockBehaviour.Properties = BlockBehaviour.Properties.of()

/**
 * [parent]の値をコピーした[BlockBehaviour.Properties]を返します。
 */
fun blockProperty(parent: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(parent)

//    BlockState    //

/**
 * 指定した[property]に紐づいた値を返します。
 * @return [BlockState.hasProperty]が`false`の場合は`null`
 */
fun <T : Comparable<T>> BlockState.getOrNull(property: Property<T>): T? = when (this.hasProperty(property)) {
    true -> getValue(property)
    false -> null
}

/**
 * 指定した[property]に紐づいた値を返します。
 * @return [BlockState.hasProperty]が`false`の場合は[default]
 */
fun <T : Comparable<T>> BlockState.getOrDefault(property: Property<T>, default: T): T = when (this.hasProperty(property)) {
    true -> getValue(property)
    false -> default
}
