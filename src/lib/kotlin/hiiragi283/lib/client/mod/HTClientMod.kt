package hiiragi283.lib.client.mod

import hiiragi283.lib.client.fluid.HTFluidModelRegister
import net.minecraft.client.color.block.BlockTintSources
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Hiiragi Seriesで使用される，クライアント側のmodの抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTClientMod {
    init {
        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::clientSetup)
        eventBus.addListener(::registerBlockColors)
        eventBus.addListener(::registerItemColors)
        eventBus.addListener { event: RegisterFluidModelsEvent -> HTFluidModelRegister(event).let(::registerFluidModels) }
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)

        initialize(eventBus, container)
    }

    /**
     * 初期化を行います。
     */
    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    /**
     * ConfigにGUIを追加します。
     */
    protected fun configScreen(container: ModContainer) {
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    /**
     * レジストリへの登録後のセットアップを行います。
     */
    protected open fun clientSetup(event: FMLClientSetupEvent) {}

    /**
     * [BlockTintSources]を登録します。
     */
    protected open fun registerBlockColors(event: RegisterColorHandlersEvent.BlockTintSources) {}

    /**
     * [ItemTintSource]を登録します。
     */
    protected open fun registerItemColors(event: RegisterColorHandlersEvent.ItemTintSources) {}

    /**
     * 液体のテクスチャを登録します。
     */
    protected open fun registerFluidModels(register: HTFluidModelRegister) {}

    /**
     * 各種クライアント側での拡張を登録します。
     */
    protected open fun registerClientExtensions(event: RegisterClientExtensionsEvent) {}

    /**
     * メニューとスクリーンの紐づけを行います。
     */
    protected open fun registerScreens(event: RegisterMenuScreensEvent) {}

    /**
     * [Entity]や[BlockEntity]のレンダラーを登録します。
     */
    protected open fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {}
}
