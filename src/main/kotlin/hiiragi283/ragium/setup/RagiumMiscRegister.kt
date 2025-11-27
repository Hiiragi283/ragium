package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.equip.HTMobEffectEquipAction
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTEntityTypeIngredient
import hiiragi283.ragium.api.registry.commonId
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.common.data.map.HTBlockCrushingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTCompressingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTCrushingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTRawSmeltingMaterialRecipe
import hiiragi283.ragium.common.inventory.slot.payload.HTFluidSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTLongSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTTeleportPosSyncPayload
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Armor Material
        event.register(Registries.ARMOR_MATERIAL) { helper ->
            register(helper, RagiumEquipmentMaterials.AZURE_STEEL)
            register(helper, RagiumEquipmentMaterials.RAGI_CRYSTAL)
            register(helper, RagiumEquipmentMaterials.DEEP_STEEL)
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper ->
            // Machine
            register(helper, RagiumRecipeTypes.ALLOYING)
            register(helper, RagiumRecipeTypes.BREWING)
            register(helper, RagiumRecipeTypes.COMPRESSING)
            register(helper, RagiumRecipeTypes.CRUSHING)
            register(helper, RagiumRecipeTypes.CUTTING)
            register(helper, RagiumRecipeTypes.EXTRACTING)
            register(helper, RagiumRecipeTypes.MELTING)
            register(helper, RagiumRecipeTypes.MIXING)
            register(helper, RagiumRecipeTypes.PLANTING)
            register(helper, RagiumRecipeTypes.REFINING)
            register(helper, RagiumRecipeTypes.SIMULATING)
            register(helper, RagiumRecipeTypes.WASHING)
        }

        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(RagiumAPI.id("entity_type"), HTEntityTypeIngredient.TYPE)
        }

        // Armor Equip Type
        event.register(RagiumAPI.EQUIP_ACTION_TYPE_KEY) { helper ->
            helper.register(RagiumAPI.id("mob_effect"), HTMobEffectEquipAction.CODEC)
        }
        // Slot Sync Type
        event.register(RagiumAPI.SLOT_TYPE_KEY) { helper ->
            helper.register(commonId("integer"), HTIntSyncPayload.STREAM_CODEC)
            helper.register(commonId("long"), HTLongSyncPayload.STREAM_CODEC)

            helper.register(vanillaId("fluid"), HTFluidSyncPayload.STREAM_CODEC)

            helper.register(RagiumAPI.id("teleport_pos"), HTTeleportPosSyncPayload.STREAM_CODEC)
        }
        // Material Recipe Type
        event.register(RagiumAPI.MATERIAL_RECIPE_TYPE_KEY) { helper ->
            helper.register(RagiumAPI.id(RagiumConst.ALLOYING, "raw"), HTRawSmeltingMaterialRecipe.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.COMPRESSING), HTCompressingMaterialRecipe.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.CRUSHING), HTCrushingMaterialRecipe.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.CRUSHING, "storage_block"), HTBlockCrushingMaterialRecipe.CODEC)
        }
    }

    @JvmStatic
    private fun register(
        helper: RegisterEvent.RegisterHelper<ArmorMaterial>,
        material: HTEquipmentMaterial,
        name: String = material.asMaterialName(),
    ) {
        val id: ResourceLocation = RagiumAPI.id(name)
        helper.register(
            id,
            ArmorMaterial(
                ArmorItem.Type.entries.associateWith(material::getArmorDefence),
                material.enchantmentValue,
                material.getEquipSound(),
                { material.repairIngredient },
                listOf(ArmorMaterial.Layer(id)),
                material.getToughness(),
                material.getKnockbackResistance(),
            ),
        )
    }

    @JvmStatic
    private fun <R : Recipe<*>> register(helper: RegisterEvent.RegisterHelper<RecipeType<*>>, holder: HTDeferredRecipeType<*, R>) {
        helper.register(holder.id, RecipeType.simple<R>(holder.id))
    }
}
