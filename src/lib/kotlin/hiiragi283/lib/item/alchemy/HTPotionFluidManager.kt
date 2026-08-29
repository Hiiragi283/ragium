package hiiragi283.lib.item.alchemy

import hiiragi283.lib.data.DataComponentSetter
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid

/**
 * 液体ポーションを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTPotionFluidManager {
    /**
     * 登録されている[液体][Fluid]の一覧
     */
    @JvmStatic
    val handlers: Map<Fluid, Handler> field: MutableMap<Fluid, Handler> = mutableMapOf()

    /**
     * 新しい液体ポーションを登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(handlers.put(fluid, handler) == null) { "Duplicated potion fluid registration: $fluid" }
    }

    /**
     * 指定した液体からハンドラを取得します。
     * @return 対応するハンドラがない場合は`null`
     */
    @JvmStatic
    fun getHandler(fluid: Fluid): Handler? = handlers[fluid]

    @JvmStatic
    fun getHandlerOrDefault(fluid: Fluid): Handler = getHandler(fluid) ?: Handler.DEFAULT

    //    Handler    //

    /**
     * ポーション瓶の種類を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface Handler {
        companion object {
            @JvmField
            val DEFAULT: Handler = object : Handler {
                override fun get(getter: DataComponentGetter): HTBottleType? = getter.get(HTPotionFluidAccess.INSTANCE.bottleType)

                override fun set(setter: DataComponentSetter, bottleType: HTBottleType) {
                    setter[HTPotionFluidAccess.INSTANCE.bottleType] = bottleType
                }
            }
        }

        operator fun get(getter: DataComponentGetter): HTBottleType?

        operator fun set(setter: DataComponentSetter, bottleType: HTBottleType)

        operator fun set(builder: DataComponentPatch.Builder, bottleType: HTBottleType) {
            set(DataComponentSetter(builder), bottleType)
        }
    }
}
