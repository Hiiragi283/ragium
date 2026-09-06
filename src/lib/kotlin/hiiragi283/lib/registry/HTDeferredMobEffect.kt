package hiiragi283.lib.registry

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.lib.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect

/**
 * [エフェクト][MobEffect]向けの[HTDeferredHolder]の拡張クラスです。
 * @param EFFECT エフェクトのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredMobEffect<out EFFECT : MobEffect> :
    HTDeferredHolder<MobEffect, EFFECT>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<MobEffect>) : super(key)

    constructor(id: Identifier) : super(Registries.MOB_EFFECT, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().displayName
}
