package hiiragi283.ragium.api.storage

import net.minecraft.core.Direction
import java.util.function.Function

fun interface HTTransferIOHolder :
    Function<Direction, HTTransferIO>,
    (Direction) -> HTTransferIO {
    companion object {
        @JvmField
        val ALWAYS = HTTransferIOHolder { HTTransferIO.BOTH }
    }

    operator fun get(direction: Direction): HTTransferIO = apply(direction)

    override fun invoke(direction: Direction): HTTransferIO = apply(direction)
}
