package hiiragi283.ragium.client.emi

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.AnimatedTextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI

/**
 * @see EmiTexture
 */
object RagiumEmiTextures {
    @JvmStatic
    fun addWidget(
        widgets: WidgetHolder,
        name: String,
        x: Int,
        y: Int,
        time: Int,
        endToStart: Boolean,
        horizontal: Boolean = false,
        fullToEmpty: Boolean = false,
    ): AnimatedTextureWidget = widgets.addAnimatedTexture(
        widget(name),
        x + 1,
        y + 1,
        1000 * time / 20,
        horizontal,
        endToStart,
        fullToEmpty,
    )

    @JvmStatic
    private fun widget(name: String): EmiTexture = EmiTexture(
        RagiumAPI.id("textures", "gui", "widgets", "$name.png"),
        0,
        0,
        16,
        16,
        16,
        16,
        16,
        16,
    )
}
