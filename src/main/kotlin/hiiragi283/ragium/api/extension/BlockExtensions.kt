package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import java.util.function.Supplier

//    Block    //

fun blockProperty(): BlockBehaviour.Properties = BlockBehaviour.Properties.of()

fun blockProperty(parent: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(parent)

fun blockProperty(parent: Supplier<Block>): BlockBehaviour.Properties = blockProperty(parent.get())

//    BlockState    //

val BlockState.tier: HTMachineTier
    get() = (block as? HTMachineTierProvider)?.tier ?: getOrDefault(HTMachineTier.PROPERTY, HTMachineTier.PRIMITIVE)

fun <T : Comparable<T>> BlockState.getOrNull(property: Property<T>): T? = when (this.hasProperty(property)) {
    true -> getValue(property)
    false -> null
}

fun <T : Comparable<T>> BlockState.getOrDefault(property: Property<T>, default: T): T = when (this.hasProperty(property)) {
    true -> getValue(property)
    false -> default
}
