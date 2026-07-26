package hiiragi283.ragium.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.item.HTCreativeModeTabHelper
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal data object RagiumMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Creative Mode Tab
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                RagiumCreativeTabs.COMMON,
                HTCreativeModeTabHelper.createSimpleTab(RagiumTranslation.RAGIUM, HTSimpleDeferredItem(CommonParts.INGOT.createId(RagiumMaterialKeys.RAGI_ALLOY))) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, RagiumItems.REGISTER.asSequence())
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, RagiumBlocks.REGISTER.asItemSequence())
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, RagiumFluids.REGISTER.asItemSequence())
                },
            )
        }
        // Data Component Type
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(RagiumAPI.id("capacity_scale"), RagiumDataComponents.CAPACITY_SCALE)
            helper.register(RagiumAPI.id("charge_power"), RagiumDataComponents.CHARGE_POWER)
            helper.register(RagiumAPI.id("loot_ticket"), RagiumDataComponents.LOOT_TICKET)
            helper.register(RagiumAPI.id("spawner_mob"), RagiumDataComponents.SPAWNER_MOB)
        }
        // Recipe Serializer
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(RagiumAPI.id("battery_combining"), RagiumRecipeSerializers.BATTERY_COMBINING)
            helper.register(RagiumAPI.id("tank_combining"), RagiumRecipeSerializers.TANK_COMBINING)

            helper.register(RagiumAPI.id(RagiumConst.ALLOYING), RagiumRecipeSerializers.ALLOYING)
            helper.register(RagiumAPI.id(RagiumConst.ASSEMBLING), RagiumRecipeSerializers.ASSEMBLING)
            helper.register(RagiumAPI.id(RagiumConst.PRINTING), RagiumRecipeSerializers.PRINTING)
            helper.register(RagiumAPI.id(RagiumConst.CUTTING), RagiumRecipeSerializers.CUTTING)
            helper.register(RagiumAPI.id(RagiumConst.COMPRESSING), RagiumRecipeSerializers.COMPRESSING)
            helper.register(RagiumAPI.id(RagiumConst.PLANTING), RagiumRecipeSerializers.PLANTING)
            helper.register(RagiumAPI.id(RagiumConst.FREEZING), RagiumRecipeSerializers.FREEZING)
            helper.register(RagiumAPI.id(RagiumConst.IMPLODING), RagiumRecipeSerializers.IMPLODING)
            helper.register(RagiumAPI.id(RagiumConst.MELTING), RagiumRecipeSerializers.MELTING)
            helper.register(RagiumAPI.id(RagiumConst.PYROLYZING), RagiumRecipeSerializers.PYROLYZING)
            helper.register(RagiumAPI.id(RagiumConst.REFINING), RagiumRecipeSerializers.REFINING)
            helper.register(RagiumAPI.id(RagiumConst.WASHING), RagiumRecipeSerializers.WASHING)
            helper.register(RagiumAPI.id(RagiumConst.BATHING), RagiumRecipeSerializers.BATHING)
            helper.register(RagiumAPI.id(RagiumConst.CHEMICAL_REACTING), RagiumRecipeSerializers.CHEMICAL_REACTING)
            helper.register(RagiumAPI.id(RagiumConst.MIXING), RagiumRecipeSerializers.MIXING)
            helper.register(RagiumAPI.id(RagiumConst.ENCHANTING, "holder"), RagiumRecipeSerializers.HOLDER_ENCHANTING)
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in RagiumRecipeTypes.allTypes) {
                helper.register(recipeType.getId(), recipeType)
            }
        }

        // Attachment Type
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES) { helper ->
            helper.register(RagiumAPI.id(RagiumConst.UNIVERSAL_CHEST), RagiumAttachmentTypes.UNIVERSAL_CHEST)
        }

        // Widget Type
        event.register(HCRegistries.Keys.WIDGET_TYPE) { helper ->
            fun register(type: HTWidgetType.Simple<*>) {
                helper.register(type.id, type)
            }

            register(RagiumWidgetTypes.ENERGY_SLOT)
        }
    }
}
