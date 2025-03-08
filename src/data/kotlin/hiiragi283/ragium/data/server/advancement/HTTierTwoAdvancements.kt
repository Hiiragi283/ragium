package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.HTDisplayInfoBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items

object HTTierTwoAdvancements : HTAdvancementGenerator("tier2/") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            title = Component.literal("Ragium - Tier 2")
            backGround = RagiumAPI.id("textures/block/ember_alloy_casing.png")
            showToast = false
            showChat = false
        }
        hasItem("has_frame", RagiumBlocks.CHEMICAL_MACHINE_FRAME)
    }

    override fun generate(registries: HolderLookup.Provider) {
        // Extractor
        val extractor: AdvancementHolder = createMachine(root, HTMachineType.EXTRACTOR)

        val beeWax: AdvancementHolder = createSimple(extractor, RagiumItems.BEE_WAX)
        // Growth Chamber
        val growth: AdvancementHolder = createMachine(root, HTMachineType.GROWTH_CHAMBER)

        val buddingAmethyst: AdvancementHolder = create("budding_amethyst", growth) {
            display {
                setIcon(Items.BUDDING_AMETHYST)
                setTitleFromItem(Items.BUDDING_AMETHYST)
                setChallenge()
            }
            hasItem("has_budding", Items.BUDDING_AMETHYST)
        }
        // Infuser
        val infuser: AdvancementHolder = createMachine(root, HTMachineType.INFUSER)

        val ambrosia: AdvancementHolder = createSimple(infuser, RagiumItems.AMBROSIA)

        val chocolate: AdvancementHolder = createSimple(infuser, RagiumItems.CHOCOLATE)

        val dynamite: AdvancementHolder = createSimple(infuser, RagiumItems.DYNAMITE)
        // Mixer
        val mixer: AdvancementHolder = createMachine(root, HTMachineType.MIXER)

        val soap: AdvancementHolder = createSimple(infuser, RagiumItems.SOAP)
        // Refinery
        val refinery: AdvancementHolder = createMachine(root, HTMachineType.REFINERY)

        val crudeOil: AdvancementHolder = createSimple(refinery, RagiumItems.CRUDE_OIL_BUCKET)
        val polymerResin: AdvancementHolder = createSimple(crudeOil, RagiumItems.POLYMER_RESIN)

        val crimsonCrystal: AdvancementHolder = createMaterial(
            refinery,
            HTTagPrefix.GEM,
            RagiumMaterials.CRIMSON_CRYSTAL,
            HTDisplayInfoBuilder::setGoal,
        )
        val warpedCrystal: AdvancementHolder = createMaterial(
            refinery,
            HTTagPrefix.GEM,
            RagiumMaterials.WARPED_CRYSTAL,
            HTDisplayInfoBuilder::setGoal,
        )
        val teleportTicket: AdvancementHolder = createSimpleConsume(
            warpedCrystal,
            RagiumItems.TELEPORT_TICKET,
            HTDisplayInfoBuilder::setGoal,
        )
        val frame: AdvancementHolder = createSimple(warpedCrystal, RagiumBlocks.PRECISION_MACHINE_FRAME, HTDisplayInfoBuilder::setChallenge)
        // Solidifier
        val solidifier: AdvancementHolder = createMachine(root, HTMachineType.SOLIDIFIER)

        val alumina: AdvancementHolder = createMaterial(solidifier, HTTagPrefix.DUST, CommonMaterials.ALUMINA)
        val aluminum: AdvancementHolder =
            createMaterial(alumina, HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
        val duralumin: AdvancementHolder =
            createMaterial(aluminum, HTTagPrefix.INGOT, RagiumMaterials.DURALUMIN, HTDisplayInfoBuilder::setGoal)
    }
}
