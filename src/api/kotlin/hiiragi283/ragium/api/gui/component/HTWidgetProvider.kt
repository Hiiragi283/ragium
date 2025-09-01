package hiiragi283.ragium.api.gui.component

import net.minecraft.client.gui.components.AbstractWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
fun interface HTWidgetProvider<T : AbstractWidget> {
    fun getWidget(): T
}
