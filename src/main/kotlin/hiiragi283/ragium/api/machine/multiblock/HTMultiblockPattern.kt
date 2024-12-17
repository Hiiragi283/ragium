package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.BiPredicate

/**
 * Represent a predicate for multiblock component
 */
interface HTMultiblockPattern : BiPredicate<World, BlockPos> {
    val text: MutableText
}
