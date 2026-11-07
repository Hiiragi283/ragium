package hiiragi283.lib.data.recipe.result

import hiiragi283.lib.data.recipe.builder.HTItemOrFluidResultAcceptor
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior

/**
 * [HTItemOrFluidResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
class HTItemOrFluidResultBuilder {
    var itemResult: HTItemResult? by HTDelegates.onceOrNull()

    var fluidResult: HTFluidResult? by HTDelegates.onceOrNull()

    fun build(): HTItemOrFluidResult = Ior.fromNullable(itemResult, fluidResult)
        ?.let(::HTItemOrFluidResult)
        ?: error("Either item or fluid result required")

    /**
     * レシピビルダーに実装されるインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface Holder : HTItemOrFluidResultAcceptor {
        val builder: HTItemOrFluidResultBuilder

        override operator fun HTItemResult.unaryPlus() {
            builder.itemResult = this
        }

        override operator fun HTFluidResult.unaryPlus() {
            builder.fluidResult = this
        }
    }
}
