package hiiragi283.ragium.client

import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.event.HTRegisterWidgetRendererEvent
import hiiragi283.lib.fluid.FluidStackTintSource
import hiiragi283.lib.fluid.HTFluidModelRegister
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.mod.HTClientMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.gui.widget.HTEnergySlotWidgetRenderer
import hiiragi283.ragium.client.gui.widget.HTFluidWidgetRenderer
import hiiragi283.ragium.client.gui.widget.HTItemWidgetRenderer
import hiiragi283.ragium.client.gui.widget.HTProgressWidgetRenderer
import hiiragi283.ragium.client.gui.widget.HTWidgetRendererManager
import hiiragi283.ragium.client.render.HTMemoryDiscClientTooltipComponent
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.ragium.common.gui.widget.RagiumWidgetTypes
import hiiragi283.ragium.common.item.tooltip.HTMemoryDiscTooltipComponent
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import net.minecraft.client.resources.model.sprite.Material
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent
import net.neoforged.neoforge.fluids.FluidStack
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
data object RagiumClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener { event: RegisterClientTooltipComponentFactoriesEvent ->
            event.register(HTMemoryDiscTooltipComponent::class.java, ::HTMemoryDiscClientTooltipComponent)
        }

        configScreen(container)
    }

    override fun clientSetup(event: FMLClientSetupEvent) {
        HTWidgetRendererManager.init()
    }

    override fun registerClientPayload(event: RegisterClientPayloadHandlersEvent) {
        event.register(HTUpdateBlockEntityPacket.TYPE, HTPayloadHandlers::handleS2C)
        event.register(HTUpdateMenuPacket.TYPE, HTPayloadHandlers::handleS2C)
    }

    override fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {
        event.register(RagiumWidgetTypes.ENERGY, ::HTEnergySlotWidgetRenderer)
        event.register(RagiumWidgetTypes.FLUID, ::HTFluidWidgetRenderer)
        event.register(RagiumWidgetTypes.ITEM, ::HTItemWidgetRenderer)
        event.register(RagiumWidgetTypes.PROGRESS, ::HTProgressWidgetRenderer)
    }

    override fun registerFluidModels(register: HTFluidModelRegister) {
        for ((color: HTDefaultColor, content: HTFluidContent) in RagiumFluids.DYES.asSequenceWithColor()) {
            register.register(content) {
                setDull()
                color.color.let(::colorTint)
            }
        }

        register.register(RagiumFluids.HONEY) {
            still = Material(vanillaId(HTConstants.BLOCK, "honey_block_top"), true)
            copyStillToFlowing()
        }
        register.register(RagiumFluids.POTION) {
            setDull()
            tintSource =
                FluidStackTintSource { stack: FluidStack ->
                    "ff000000".hexToInt() or
                        HTPotionHelper.getPotion(stack).color
                }
        }
        register.register(RagiumFluids.OMINOUS_FLUX) {
            setMolten()
            colorTint(Color(0x003366))
        }

        register.register(RagiumFluids.MOLTEN_GLASS) {
            setMolten()
            colorTint(Color(0xffffff))
        }
        register.register(RagiumFluids.MOLTEN_REDSTONE) {
            setMolten()
            colorTint(Color(0xcc0000))
        }
        register.register(RagiumFluids.MOLTEN_GLOWSTONE) {
            setMolten()
            colorTint(Color(0xffcc66))
        }
        register.register(RagiumFluids.MOLTEN_ENDER) {
            setMolten()
            colorTint(Color(0x006666))
        }
        register.register(RagiumFluids.MOLTEN_BLAZE) {
            setMolten()
            colorTint(Color(0xff9900))
        }

        register.register(RagiumFluids.HYDROGEN) {
            setClear()
            colorTint(Color(0x003399))
        }
        register.register(RagiumFluids.OXYGEN) {
            setClear()
            colorTint(Color(0x3399cc))
        }
        register.register(RagiumFluids.CHLORINE) {
            setClear()
            colorTint(Color(0x99cc33))
        }

        register.register(RagiumFluids.CREOSOTE) {
            setDull()
            colorTint(Color(0x663333))
        }
        register.register(RagiumFluids.CRUDE_OIL) {
            setDull()
            colorTint(Color(0x333333))
        }
        register.register(RagiumFluids.NAPHTHA) {
            setClear()
            colorTint(Color(0xff6600))
        }
        register.register(RagiumFluids.FUEL) {
            setClear()
            colorTint(Color(0xff9900))
        }
        register.register(RagiumFluids.AROMATIC_COMPOUND) {
            setClear()
            colorTint(Color(0xcccc66))
        }
        register.register(RagiumFluids.NAOH_SOLUTION) {
            setDull()
            colorTint(Color(0x003366))
        }
        register.register(RagiumFluids.SULFUR_DIOXIDE) {
            setClear()
            colorTint(Color(0x996600))
        }
        register.register(RagiumFluids.SULFUR_TRIOXIDE) {
            setClear()
            colorTint(Color(0xcc9900))
        }
        register.register(RagiumFluids.SULFURIC_ACID) {
            setDull()
            colorTint(Color(0xcc9900))
        }
        register.register(RagiumFluids.HYDROGEN_CHLORIDE) {
            setClear()
            colorTint(Color(0x66cc33))
        }
        register.register(RagiumFluids.HYDROCHLORIC_ACID) {
            setDull()
            colorTint(Color(0x66cc33))
        }
        register.register(RagiumFluids.CAOH_SOLUTION) {
            setDull()
            colorTint(Color(0x336699))
        }
        register.register(RagiumFluids.MOLTEN_STEEL) {
            setMolten()
            colorTint(Color(0x999999))
        }
    }

    override fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(HTBlockWidgetHolderContext.MENU_TYPE.get(), ::HTWidgetContainerScreen)
    }
}
