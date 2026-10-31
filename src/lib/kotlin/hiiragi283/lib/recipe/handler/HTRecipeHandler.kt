package hiiragi283.lib.recipe.handler

import hiiragi283.lib.HTConstants
import hiiragi283.lib.util.fixedFraction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTRecipeHandler :
    ValueIOSerializable,
    Runnable {
    protected var progress: Int = 0
    protected var maxProgress: Int = 0

    val progression: Float get() = fixedFraction(progress, maxProgress)

    protected fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
    }

    protected var shouldCheck: Boolean = true
    protected var canProgress: Boolean = false

    override fun run() {
        shouldCheck = true
    }

    abstract fun tick(level: ServerLevel): Boolean

    override fun serialize(output: ValueOutput) {
        output.putInt(HTConstants.PROGRESS, progress)
        output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
    }

    override fun deserialize(input: ValueInput) {
        input.getInt(HTConstants.MAX_PROGRESS).ifPresent { maxProgress: Int ->
            this.maxProgress = maxProgress
            input.getInt(HTConstants.PROGRESS).ifPresent { progress: Int ->
                this.progress = progress
            }
        }
    }

    //    Progressor    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Progressor {
        fun getProgress(): Int
    }
}
