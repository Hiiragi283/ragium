package hiiragi283.lib.fluid

import hiiragi283.lib.text.MutableText
import hiiragi283.lib.text.withStyle
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * Hiiragi Seriesで使用される[FluidType]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTFluidType(properties: Properties) : FluidType(properties) {
    /**
     * 表示名の色を取得します。
     * @return 色を付けない場合は`null`
     */
    protected open fun getNameColor(stack: FluidStack): TextColor? = null

    override fun getDescription(stack: FluidStack): Component {
        var name: MutableText = super.getDescription(stack).copy()
        val color: TextColor? = getNameColor(stack)
        if (color != null) {
            name = name.withStyle(color)
        }
        return name
    }

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)
}
