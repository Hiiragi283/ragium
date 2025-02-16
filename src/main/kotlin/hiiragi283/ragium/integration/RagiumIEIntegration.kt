package hiiragi283.ragium.integration

import blusunrize.immersiveengineering.api.tool.RailgunHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import java.util.*

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RagiumAPI.MOD_ID)
object RagiumIEIntegration {
    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
        // Dynamite
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItems.DYNAMITE) },
            object : RailgunHandler.IRailgunProjectile {
                override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity? {
                    if (shooter != null) {
                        ammo.consume(1, shooter)
                        val dynamite = HTDynamite(shooter.level(), shooter)
                        dynamite.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 3f, 0f)
                        return dynamite
                    }
                    return super.getProjectile(shooter, ammo, defaultProjectile)
                }
            },
        )

        // Creeper Reagent
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItems.CREEPER_REAGENT) },
            object : RailgunHandler.IRailgunProjectile {
                override fun onHitTarget(
                    world: Level,
                    target: HitResult,
                    shooter: UUID?,
                    projectile: Entity,
                ) {
                    world.explode(
                        null,
                        target.location.x,
                        target.location.y,
                        target.location.z,
                        4f,
                        false,
                        Level.ExplosionInteraction.NONE,
                    )
                }
            },
        )

        // Glow Reagent
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItems.GLOW_REAGENT) },
            object : RailgunHandler.IRailgunProjectile {
                override fun onHitTarget(
                    world: Level,
                    target: HitResult,
                    shooter: UUID?,
                    projectile: Entity,
                ) {
                    if (target is EntityHitResult) {
                        val entity: LivingEntity = target.entity as? LivingEntity ?: return
                        entity.addEffect(MobEffectInstance(MobEffects.GLOWING, -1))
                    }
                }
            },
        )
    }
}
