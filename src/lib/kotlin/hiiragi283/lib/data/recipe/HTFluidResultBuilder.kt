package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.VanillaFluidContents
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.properties.Delegates
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * [HTFluidResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class HTFluidResultBuilder {
    @PublishedApi internal var result: HTFluidResult by Delegates.notNull()

    var amount: Int
        get() = result.amount
        set(value) {
            result = result.copyWithAmount(value)
        }

    operator fun FluidStackTemplate.unaryPlus() {
        result = HTFluidResult.create(this)
    }

    operator fun Fluid.unaryPlus() {
        +FluidStackTemplate(this, FluidType.BUCKET_VOLUME)
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.toTemplate()
    }

    fun water() {
        +VanillaFluidContents.WATER
    }

    fun lava() {
        +VanillaFluidContents.LAVA
    }

    fun milk() {
        +VanillaFluidContents.MILK
    }

    fun build(): HTFluidResult = result
}
