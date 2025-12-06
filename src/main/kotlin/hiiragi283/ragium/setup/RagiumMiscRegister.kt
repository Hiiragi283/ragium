package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.equip.HTMobEffectEquipAction
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTEntityTypeIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTPotionIngredient
import hiiragi283.ragium.api.recipe.ingredient.RagiumIngredientTypes
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.commonId
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.common.data.map.HTBlockCrushingRecipeProvider
import hiiragi283.ragium.common.data.map.HTCompressingRecipeProvider
import hiiragi283.ragium.common.data.map.HTCrushingRecipeProvider
import hiiragi283.ragium.common.data.map.HTRawSmeltingRecipeProvider
import hiiragi283.ragium.common.inventory.slot.payload.HTFluidSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTLongSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTTeleportPosSyncPayload
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
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
            register(helper, RagiumRecipeTypes.ENCHANTING)
            register(helper, RagiumRecipeTypes.EXTRACTING)
            register(helper, RagiumRecipeTypes.MELTING)
            register(helper, RagiumRecipeTypes.MIXING)
            register(helper, RagiumRecipeTypes.PLANTING)
            register(helper, RagiumRecipeTypes.REFINING)
            register(helper, RagiumRecipeTypes.SIMULATING)
        }

        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            fun <T : ICustomIngredient> register(
                holder: HTDeferredHolder<IngredientType<*>, IngredientType<T>>,
                codec: MapBiCodec<RegistryFriendlyByteBuf, T>,
            ) {
                helper.register(holder.id, codec.toSerializer(::IngredientType))
            }

            register(RagiumIngredientTypes.ENTITY_TYPE, HTEntityTypeIngredient.CODEC)
            register(RagiumIngredientTypes.POTION, HTPotionIngredient.CODEC)
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
        // Runtime Recipe Type
        event.register(RagiumAPI.RUNTIME_RECIPE_TYPE_KEY) { helper ->
            helper.register(RagiumAPI.id(RagiumConst.ALLOYING, "raw"), HTRawSmeltingRecipeProvider.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.COMPRESSING), HTCompressingRecipeProvider.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.CRUSHING), HTCrushingRecipeProvider.CODEC)
            helper.register(RagiumAPI.id(RagiumConst.CRUSHING, "storage_block"), HTBlockCrushingRecipeProvider.CODEC)
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
