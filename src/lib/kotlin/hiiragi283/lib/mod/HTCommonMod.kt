package hiiragi283.lib.mod

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Hiiragi Seriesで使用される，共通部分のmodの抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTCommonMod {
    init {
        NeoForgeMod.enableMilkFluid()

        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::commonSetup)
        eventBus.addListener { event: RegisterPayloadHandlersEvent ->
            container.modInfo.version
                .toString()
                .let(event::registrar)
                .let(::registerPayload)
        }

        initialize(eventBus, container)
    }

    /**
     * 初期化を行います。
     */
    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    /**
     * レジストリへの登録後のセットアップを行います。
     */
    protected open fun commonSetup(event: FMLCommonSetupEvent) {}

    /**
     * パケットを登録します。
     */
    protected open fun registerPayload(registrar: PayloadRegistrar) {}
}
