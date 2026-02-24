package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

abstract class HTChancedRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    lateinit var result: HTItemResult
    val extraResult: ExtraResultHolder = ExtraResultHolder()

    inner class ExtraResultHolder {
        var result: HTChancedItemResult? = null
            private set

        @JvmName("setResult")
        operator fun plusAssign(result: HTItemResult) {
            this.plusAssign(result to Fraction.ONE)
        }

        @JvmName("setResultWithChance")
        operator fun plusAssign(result: HTChancedItemResult) {
            check(this.result == null) { "Extra Result has already beed initialized" }
            this.result = result
        }

        fun toOptional(): Optional<HTChancedItemResult> = result.wrapOptional()
    }

    final override fun getPrimalId(): ResourceLocation = result.getId()
}
