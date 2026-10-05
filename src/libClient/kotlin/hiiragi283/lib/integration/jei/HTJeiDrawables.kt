package hiiragi283.lib.integration.jei

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.util.HTDelegates
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper

/**
 * JEIで使用するスプライトをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTJeiDrawables {
    @JvmStatic
    var slots: Map<HTBackgroundType, IDrawable> by HTDelegates.onceInitialize()
        private set

    @JvmStatic
    var tanks: Map<HTBackgroundType, IDrawable> by HTDelegates.onceInitialize()
        private set

    @JvmStatic
    fun init(guiHelper: IGuiHelper) {
        slots = HTBackgroundType.entries.associateWithTo(mutableEnumMapOf()) { type: HTBackgroundType ->
            guiHelper
                .drawableBuilder(type.slotTexture, 0, 0, 18, 18)
                .setTextureSize(18, 18)
                .build()
        }
        tanks = HTBackgroundType.entries.associateWithTo(mutableEnumMapOf()) { type: HTBackgroundType ->
            guiHelper
                .drawableBuilder(type.tankTexture, 0, 0, 18, 18 * 3)
                .setTextureSize(18, 18)
                .build()
        }
    }

    /**
     * スロットの背景スプライトを取得します。
     */
    @JvmStatic
    fun getSlot(type: HTBackgroundType): IDrawable = slots[type]!!

    /**
     * タンクの背景スプライトを取得します。
     */
    @JvmStatic
    fun getTank(type: HTBackgroundType): IDrawable = tanks[type]!!
}
