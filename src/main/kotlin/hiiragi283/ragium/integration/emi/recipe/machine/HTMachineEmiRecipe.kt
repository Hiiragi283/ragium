package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

abstract class HTMachineEmiRecipe(id: ResourceLocation, private val textureId: ResourceLocation) : HTEmiRecipe.Impl(id) {
    final override fun getDisplayWidth(): Int = getPosition(7)

    final override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(
            EmiTexture(
                textureId,
                HTSlotHelper.getSlotPosX(1) - 1,
                HTSlotHelper.getSlotPosY(0) - 1,
                getPosition(7),
                getPosition(3),
            ),
            0,
            0,
        )
        widgets.addArrow(arrowPosX, arrowPosY)
    }

    protected open val arrowPosX: Int = getPosition(2.5)
    protected open val arrowPosY: Int = getPosition(1)
}
