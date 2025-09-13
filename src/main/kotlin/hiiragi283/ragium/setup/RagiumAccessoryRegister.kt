package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.accessory.HTDynamicLightingAccessory
import hiiragi283.ragium.common.accessory.HTMagnetizationAccessory
import hiiragi283.ragium.common.accessory.HTMobEffectAccessory
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ItemLike

object RagiumAccessoryRegister {
    @JvmStatic
    fun register() {
        // Charm
        register(RagiumItems.DYNAMIC_LANTERN, HTDynamicLightingAccessory)
        register(
            RagiumItems.MAGNET,
            HTMagnetizationAccessory.create { entity: ItemEntity, player: Player ->
                // IEのコンベヤ上にいるアイテムは無視する
                if (entity.persistentData.getBoolean(RagiumConst.PREVENT_ITEM_MAGNET)) return@create
                if (entity.isAlive && !entity.hasPickUpDelay()) {
                    entity.playerTouch(player)
                }
            },
        )
        register(
            RagiumItems.ADVANCED_MAGNET,
            HTMagnetizationAccessory.create { entity: ExperienceOrb, player: Player ->
                if (entity.value > 0) {
                    entity.playerTouch(player)
                }
            },
        )

        // Face
        register(RagiumItems.NIGHT_VISION_GOGGLES, HTMobEffectAccessory(MobEffects.NIGHT_VISION, -1, ambient = true))
    }

    @JvmStatic
    private fun register(item: ItemLike, accessory: Accessory) {
        AccessoriesAPI.registerAccessory(item.asItem(), accessory)
    }
}
