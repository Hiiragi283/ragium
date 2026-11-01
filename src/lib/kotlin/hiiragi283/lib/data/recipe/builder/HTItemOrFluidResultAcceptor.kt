package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTItemOrFluidResultAcceptor {
    operator fun HTItemResult.unaryPlus()

    operator fun HTFluidResult.unaryPlus()

    fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        +HTItemResultBuilder.build(builderAction)
    }

    fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        +HTFluidResultBuilder.build(builderAction)
    }
}
