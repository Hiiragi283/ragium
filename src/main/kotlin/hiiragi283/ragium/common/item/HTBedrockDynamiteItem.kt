package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.common.entity.HTBedrockDynamiteEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class HTBedrockDynamiteItem(settings: Settings) : HTThrowableItem(settings.descriptions(RagiumTranslationKeys.BEDROCK_DYNAMITE)) {
    override fun createEntity(world: World, user: LivingEntity): ProjectileEntity = HTBedrockDynamiteEntity(world, user)

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTBedrockDynamiteEntity(world, pos.x, pos.y, pos.z)
}
