package hiiragi283.lib.integration.jei

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.gui.HTBackgroundType
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper

/**
 * JEIで使用するスプライトをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
object HTJeiDrawables {
    @JvmStatic
    private val SLOTS: MutableMap<HTBackgroundType, IDrawable> = mutableEnumMapOf()

    @JvmStatic
    private val TANKS: MutableMap<HTBackgroundType, IDrawable> = mutableEnumMapOf()

    /**
     * スロットの背景スプライトを取得します。
     */
    @JvmStatic
    fun getSlot(type: HTBackgroundType, guiHelper: IGuiHelper): IDrawable = SLOTS.computeIfAbsent(type) { typeIn: HTBackgroundType ->
        guiHelper
            .drawableBuilder(typeIn.slotTexture, 0, 0, 18, 18)
            .setTextureSize(18, 18)
            .build()
    }

    /**
     * タンクの背景スプライトを取得します。
     */
    @JvmStatic
    fun getTank(type: HTBackgroundType, guiHelper: IGuiHelper): IDrawable = TANKS.computeIfAbsent(type) { typeIn: HTBackgroundType ->
        guiHelper
            .drawableBuilder(typeIn.tankTexture, 0, 0, 18, 18 * 3)
            .setTextureSize(18, 18 * 3)
            .build()
    }
}
