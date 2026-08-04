package hiiragi283.lib.item.alchemy

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
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
    val handlers: Map<Fluid, Handler>field: MutableMap<Fluid, Handler> = hashMapOf()

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
    fun getFluidHandler(fluid: Fluid): Handler? = handlers[fluid]

    //    Handler    //

    /**
     * ポーション瓶の種類を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface Handler {
        operator fun get(getter: DataComponentGetter): HTBottleType?

        operator fun set(builder: DataComponentMap.Builder, bottleType: HTBottleType)

        operator fun set(builder: DataComponentPatch.Builder, bottleType: HTBottleType)
    }
}
