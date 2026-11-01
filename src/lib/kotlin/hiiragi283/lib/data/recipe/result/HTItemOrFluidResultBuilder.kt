package hiiragi283.lib.data.recipe.result

import hiiragi283.lib.data.recipe.builder.HTItemOrFluidResultAcceptor
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import java.util.Optional

/**
 * [HTItemOrFluidResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
class HTItemOrFluidResultBuilder {
    var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    var fluidResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    val result: HTItemOrFluidResult by lazy {
        Ior.fromNullable(itemResult, fluidResult)
            .map(::HTItemOrFluidResult)
            .orElseThrow { error("Either item or fluid result required") }
    }

    /**
     * レシピビルダーに実装されるインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface Holder : HTItemOrFluidResultAcceptor {
        val builder: HTItemOrFluidResultBuilder

        val result: HTItemOrFluidResult get() = builder.result

        override operator fun HTItemResult.unaryPlus() {
            builder.itemResult = Optional.of(this)
        }

        override operator fun HTFluidResult.unaryPlus() {
            builder.fluidResult = Optional.of(this)
        }
    }
}
