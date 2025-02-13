package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalBuilder
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.SlurryRegistryObject
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import org.slf4j.Logger

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object RagiumMekIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGINITE_SLURRY: SlurryRegistryObject<Chemical, Chemical> =
        CHEMICAL_REGISTER.registerSlurry("raginite") { builder: ChemicalBuilder ->
            builder.tint(0xff003f).ore(HTTagPrefix.ORE.createTag(RagiumMaterials.RAGINITE))
        }

    @SubscribeEvent
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(IntegrationMaterials.REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        LOGGER.info("Enabled Mekanism Integration!")
    }
}
