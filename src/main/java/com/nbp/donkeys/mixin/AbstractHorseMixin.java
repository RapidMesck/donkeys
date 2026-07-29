package com.nbp.donkeys.mixin;

import com.nbp.donkeys.DonkeysMod;
import com.nbp.donkeys.entity.CustomDonkeyEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractHorseMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void donkeys$onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        AbstractChestedHorse self = (AbstractChestedHorse) (Object) this;

        if (!(self instanceof Donkey) || self instanceof CustomDonkeyEntity) {
            return;
        }

        Donkey donkey = (Donkey) self;
        ItemStack held = player.getItemInHand(hand);

        if (held.is(DonkeysMod.GALA_APPLE.get()) && donkey.isTamed()) {
            if (donkey.level() instanceof ServerLevel) {
                CustomDonkeyEntity.convert(donkey, player, held);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
