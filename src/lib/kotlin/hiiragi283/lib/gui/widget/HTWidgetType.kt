package hiiragi283.lib.gui.widget

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.HTKeyOrValue
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [HTWidget]を識別するためのインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTWidgetType<WIDGET : HTWidget>(private val id: Identifier) :
    HTKeyOrValue<HTWidgetType<*>, HTWidgetType<WIDGET>> {
    override fun unwrapWithKey(): Ior<ResourceKey<HTWidgetType<*>>, HTWidgetType<WIDGET>> =
        Ior.Both(RagiumRegistries.Keys.WIDGET_TYPE.createKey(id), this)

    override fun toString(): String = "HTWidgetType(id=$id)"
}
