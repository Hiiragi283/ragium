package hiiragi283.ragium.mixin;

import hiiragi283.ragium.common.InternalRagiumAPI;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Registries.class, priority = 999)
public abstract class RegistriesMixin {
    @Inject(method = "freezeRegistries", at = @At("HEAD"))
    private static void ragium$bindMaterialToItem(CallbackInfo ci) {
        InternalRagiumAPI.registerMaterials();
    }
}
