package hiiragi283.ragium.integration.mekanism

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.chemical.Chemical
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import mekanism.common.registries.MekanismItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.registries.DeferredItem

@HTAddon(RagiumConstantValues.MEKANISM)
object RagiumMekanismAddon : RagiumAddon {
    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val CHEMICAL_RAGINITE: DeferredChemical<Chemical> = CHEMICAL_REGISTER.registerInfuse("raginite", 0xFF0033)

    @JvmField
    val CHEMICAL_AZURE: DeferredChemical<Chemical> = CHEMICAL_REGISTER.registerInfuse("azure", 0x9999cc)

    @JvmField
    val CHEMICAL_CRIMSON_SAP: DeferredChemical<Chemical> =
        CHEMICAL_REGISTER.register(RagiumChemicalConstants.CRIMSON_SAP)

    @JvmField
    val CHEMICAL_WARPED_SAP: DeferredChemical<Chemical> =
        CHEMICAL_REGISTER.register(RagiumChemicalConstants.WARPED_SAP)

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_ENRICHED_RAGINITE: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("enriched_raginite")

    @JvmField
    val ITEM_ENRICHED_AZURE: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("enriched_azure")

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)
        eventBus.addListener(::modifyComponent)

        CHEMICAL_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponent(event: ModifyDefaultComponentsEvent) {
        event.modify(MekanismItems.YELLOW_CAKE_URANIUM) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            // Raginite
            event.insertAfter(
                RagiumItems.RAGI_COKE.toStack(),
                ITEM_ENRICHED_RAGINITE.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )

            // Azure
            event.insertAfter(
                RagiumItems.AZURE_SHARD.toStack(),
                ITEM_ENRICHED_AZURE.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
        }
    }
}
