package hiiragi283.ragium.client.integration.emi.handler

import dev.emi.emi.api.EmiExclusionArea
import dev.emi.emi.api.widget.Bounds
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.client.gui.component.base.HTAbstractWidget
import hiiragi283.ragium.client.gui.screen.HTContainerScreen
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.client.gui.screens.Screen
import java.util.function.Consumer

data object RagiumEmiExclusionArea : EmiExclusionArea<Screen> {
    override fun addExclusionArea(screen: Screen, consumer: Consumer<Bounds>) {
        if (screen is HTContainerScreen<*>) {
            val screenBounds: HTBounds = screen.getBounds()
            screen
                .children()
                .filterIsInstance<HTAbstractWidget>()
                .filterNot(screenBounds::contains)
                .map(HTAbstractWidget::getBounds)
                .map(HTBounds::toEmi)
                .forEach(consumer)
        }
    }
}
